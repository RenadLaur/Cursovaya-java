package kz.enu.restaurant.repository;

import kz.enu.restaurant.model.Dish;

import java.util.List;
import java.util.Optional;
/**
 * Интерфейс доступа к данным меню.
 * Позволяет получать список блюд и искать конкретное блюдо по идентификатору.
 */

public interface MenuRepository {
    /**
     * Возвращает полный список блюд.
     *
     * @return список всех блюд меню
     */
    List<Dish> findAllDishes();
    /**
     * Ищет блюдо по его идентификатору.
     *
     * @param id идентификатор блюда
     * @return Optional с блюдом, если найдено
     */
    Optional<Dish> findById(long id);
}
