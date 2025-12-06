package kz.enu.restaurant.repository;

import kz.enu.restaurant.exception.MenuDataException;
import kz.enu.restaurant.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * Репозиторий меню, загружающий данные из CSV-файлов.
 * Использует ingredients.csv и dishes.csv в каталоге ресурсов.
 */
@Repository
public class FileMenuRepository implements MenuRepository {

    private static final Logger log = LoggerFactory.getLogger(FileMenuRepository.class);

    private static final String INGREDIENTS_FILE = "src/main/resources/data/ingredients.csv";
    private static final String DISHES_FILE = "src/main/resources/data/dishes.csv";

    private final List<Dish> dishes;

    /**
     * При создании репозитория сразу загружаем все блюда в память.
     * При ошибке чтения бросаем MenuDataException.
     */
    public FileMenuRepository() {
        this.dishes = loadDishes();
        log.info("Меню успешно загружено: {} блюд", dishes.size());
    }

    @Override
    public List<Dish> findAllDishes() {
        return Collections.unmodifiableList(dishes);
    }

    @Override
    public Optional<Dish> findById(long id) {
        return dishes.stream()
                .filter(d -> d.getId() == id)
                .findFirst();
    }

    /**
     * Загрузка списка ингредиентов из CSV-файла.
     */
    private List<Ingredient> loadIngredients() {
        List<Ingredient> ingredients = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get(INGREDIENTS_FILE));
            for (String raw : lines) {
                String line = raw.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }
                String[] parts = line.split(";");
                if (parts.length < 4) {
                    log.warn("Пропускаю строку ингредиента (мало колонок): {}", line);
                    continue;
                }

                long id = Long.parseLong(parts[0].trim());
                String name = parts[1].trim();
                double caloriesPer100g = Double.parseDouble(parts[2].trim().replace(',', '.'));
                double pricePerKg = Double.parseDouble(parts[3].trim().replace(',', '.'));

                ingredients.add(new Ingredient(id, name, caloriesPer100g, pricePerKg));
            }
            log.info("Загружено {} ингредиентов из {}", ingredients.size(), INGREDIENTS_FILE);
        } catch (IOException e) {
            log.error("Ошибка чтения файла ингредиентов: {}", INGREDIENTS_FILE, e);
            throw new MenuDataException("Не удалось прочитать файл ингредиентов: " + INGREDIENTS_FILE, e);
        } catch (NumberFormatException e) {
            log.error("Ошибка парсинга числовых значений в файле ингредиентов", e);
            throw new MenuDataException("Неверный числовой формат в файле ингредиентов", e);
        }
        return ingredients;
    }

    /**
     * Загрузка списка блюд из CSV-файла и привязка к ингредиентам.
     */
    private List<Dish> loadDishes() {
        List<Ingredient> ingredients = loadIngredients();
        Map<Long, Ingredient> ingredientMap = new HashMap<>();
        for (Ingredient ing : ingredients) {
            ingredientMap.put(ing.getId(), ing);
        }

        List<Dish> dishes = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get(DISHES_FILE));
            for (String raw : lines) {
                String line = raw.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }
                String[] parts = line.split(";");
                if (parts.length < 3) {
                    log.warn("Пропускаю строку блюда (мало колонок): {}", line);
                    continue;
                }

                long id = Long.parseLong(parts[0].trim());
                String name = parts[1].trim();
                String typeStr = parts[2].trim().toUpperCase(Locale.ROOT);

                DishType type;
                try {
                    type = DishType.valueOf(typeStr);
                } catch (IllegalArgumentException e) {
                    log.warn("Неизвестный тип блюда '{}' в строке: {}. Использую MAIN_COURSE по умолчанию.",
                            typeStr, line);
                    type = DishType.MAIN_COURSE;
                }

                Dish dish = new Dish(id, name, type);

                if (parts.length > 3 && !parts[3].isBlank()) {
                    String ingredientsSpec = parts[3].trim(); // "1:200,3:150"
                    String[] items = ingredientsSpec.split(",");
                    for (String itemRaw : items) {
                        String item = itemRaw.trim();
                        if (item.isEmpty()) continue;

                        String[] ingParts = item.split(":");
                        if (ingParts.length != 2) {
                            log.warn("Пропускаю некорректную запись ингредиента: {}", item);
                            continue;
                        }

                        long ingrId = Long.parseLong(ingParts[0].trim());
                        double grams = Double.parseDouble(ingParts[1].trim().replace(',', '.'));

                        Ingredient ingredient = ingredientMap.get(ingrId);
                        if (ingredient == null) {
                            log.warn("Ингредиент с id={} не найден для блюда id={}", ingrId, id);
                            continue;
                        }

                        dish.addIngredient(new DishIngredient(ingredient, grams));
                    }
                }

                dishes.add(dish);
            }
            log.info("Загружено {} блюд из {}", dishes.size(), DISHES_FILE);
        } catch (IOException e) {
            log.error("Ошибка чтения файла блюд: {}", DISHES_FILE, e);
            throw new MenuDataException("Не удалось прочитать файл блюд: " + DISHES_FILE, e);
        } catch (NumberFormatException e) {
            log.error("Ошибка парсинга числовых значений в файле блюд", e);
            throw new MenuDataException("Неверный числовой формат в файле блюд", e);
        }
        return dishes;
    }
}
