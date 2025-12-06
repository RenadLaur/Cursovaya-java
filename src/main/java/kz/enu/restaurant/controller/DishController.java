package kz.enu.restaurant.controller;

import kz.enu.restaurant.model.Dish;
import kz.enu.restaurant.model.DishIngredient;
import kz.enu.restaurant.service.MenuService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
/**
 * REST-контроллер для работы с блюдами меню.
 * Предоставляет API для получения списка блюд и деталей конкретного блюда.
 */
@RestController
@RequestMapping("/api/dishes")
@CrossOrigin(origins = "*")   // чтобы фронтенд с другого origin мог делать запросы
public class DishController {

    private final MenuService menuService;

    public DishController(MenuService menuService) {
        this.menuService = menuService;
    }
    /**
     * Возвращает перечень всех блюд меню в кратком виде.
     *
     * @return список DTO с основными характеристиками блюд
     */
    @GetMapping
    public List<DishDto> getAllDishes() {
        List<Dish> dishes = menuService.getAllDishes();
        return dishes.stream()
                .map(d -> new DishDto(
                        d.getId(),
                        d.getName(),
                        d.getType(),
                        d.getTotalCalories(),
                        d.getTotalCost()
                ))
                .collect(Collectors.toList());
    }
    /**
     * Возвращает подробную информацию по одному блюду.
     *
     * @param id идентификатор блюда
     * @return HTTP 200 и данные блюда или 404, если блюдо не найдено
     */
    @GetMapping("/{id}")
    public ResponseEntity<DishDetailsDto> getDish(@PathVariable("id") long id) {
        return menuService.getDishById(id)
                .map(dish -> {
                    var ingredientLines = dish.getIngredients().stream()
                            .map(this::mapIngredient)
                            .toList();

                    DishDetailsDto dto = new DishDetailsDto(
                            dish.getId(),
                            dish.getName(),
                            dish.getType(),
                            dish.getTotalCalories(),
                            dish.getTotalCost(),
                            ingredientLines
                    );
                    return ResponseEntity.ok(dto);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    private DishDetailsDto.IngredientLine mapIngredient(DishIngredient di) {
        double calories = di.getIngredient().getCaloriesPer100g() * di.getGrams() / 100.0;
        double cost = di.getIngredient().getPricePerKg() * di.getGrams() / 1000.0;
        return new DishDetailsDto.IngredientLine(
                di.getIngredient().getName(),
                di.getGrams(),
                calories,
                cost
        );
    }
}
