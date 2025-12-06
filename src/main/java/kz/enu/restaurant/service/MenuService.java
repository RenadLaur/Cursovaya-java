package kz.enu.restaurant.service;

import kz.enu.restaurant.model.Dish;
import kz.enu.restaurant.repository.MenuRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MenuService {

    private final MenuRepository menuRepository;

    public MenuService(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    public List<Dish> getAllDishes() {
        return menuRepository.findAllDishes();
    }

    public Optional<Dish> getDishById(long id) {
        return menuRepository.findById(id);
    }
}
