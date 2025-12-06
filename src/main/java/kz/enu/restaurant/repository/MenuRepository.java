package kz.enu.restaurant.repository;

import kz.enu.restaurant.model.Dish;

import java.util.List;
import java.util.Optional;

public interface MenuRepository {

    List<Dish> findAllDishes();

    Optional<Dish> findById(long id);
}
