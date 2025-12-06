package kz.enu.restaurant.repository;

import kz.enu.restaurant.model.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * ВРЕМЕННАЯ версия репозитория.
 * Никаких файлов, просто в памяти создаём пару блюд.
 * Нужна, чтобы проверить, что модели, сервис и контроллер работают.
 */
@Repository
public class FileMenuRepository implements MenuRepository {

    private final List<Dish> dishes = new ArrayList<>();

    public FileMenuRepository() {
        // Создаём тестовые ингредиенты
        Ingredient chicken = new Ingredient(1, "Куриное филе", 165, 2200);
        Ingredient rice = new Ingredient(2, "Рис", 340, 600);
        Ingredient oil = new Ingredient(3, "Оливковое масло", 884, 4500);

        // Блюдо 1
        Dish dish1 = new Dish(1, "Курица с рисом", DishType.MAIN_COURSE);
        dish1.addIngredient(new DishIngredient(chicken, 200)); // 200 г курицы
        dish1.addIngredient(new DishIngredient(rice, 150));    // 150 г риса
        dish1.addIngredient(new DishIngredient(oil, 10));      // 10 г масла

        // Блюдо 2
        Dish dish2 = new Dish(2, "Просто рис", DishType.STARTER);
        dish2.addIngredient(new DishIngredient(rice, 200));

        dishes.add(dish1);
        dishes.add(dish2);
    }

    @Override
    public List<Dish> findAllDishes() {
        return dishes;
    }

    @Override
    public Optional<Dish> findById(long id) {
        return dishes.stream()
                .filter(d -> d.getId() == id)
                .findFirst();
    }
}
