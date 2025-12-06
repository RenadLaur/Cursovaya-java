package kz.enu.restaurant.service;

import kz.enu.restaurant.model.Dish;
import kz.enu.restaurant.repository.MenuRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
/**
 * Сервисный слой для работы с меню.
 * Инкапсулирует бизнес-логику поверх репозитория.
 */
@Service
public class MenuService {

    private final MenuRepository menuRepository;

    public MenuService(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }
    /**
     * Возвращает список всех блюд меню.
     *
     * @return список блюд
     */
    public List<Dish> getAllDishes() {
        return menuRepository.findAllDishes();
    }
    /**
     * Ищет блюдо по его идентификатору.
     *
     * @param id идентификатор блюда
     * @return Optional с найденным блюдом
     */
    public Optional<Dish> getDishById(long id) {
        return menuRepository.findById(id);
    }
}
