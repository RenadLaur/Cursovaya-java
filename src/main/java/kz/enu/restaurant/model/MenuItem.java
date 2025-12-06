package kz.enu.restaurant.model;

public abstract class MenuItem {

    private long id;
    private String name;
    private DishType type;

    protected MenuItem(long id, String name, DishType type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public DishType getType() {
        return type;
    }

    public abstract double getTotalCalories();

    public abstract double getTotalCost();
}
