package kz.enu.restaurant.model;
/**
 * Абстрактный элемент меню ресторана.
 * Определяет общие свойства для блюд и напитков.
 */
public abstract class MenuItem {

    private long id;
    private String name;
    private DishType type;

    protected MenuItem(long id, String name, DishType type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }
    /** @return уникальный идентификатор элемента меню */
    public long getId() {
        return id;
    }
    /** @return название блюда или напитка */
    public String getName() {
        return name;
    }

    public DishType getType() {
        return type;
    }
    /**
     * @return полная калорийность элемента меню в килокалориях.
     */
    public abstract double getTotalCalories();
/**
 * @return полная стоимость элемента меню в тенге.
 */
    public abstract double getTotalCost();
}
