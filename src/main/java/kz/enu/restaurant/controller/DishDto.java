package kz.enu.restaurant.controller;

import kz.enu.restaurant.model.DishType;

public class DishDto {

    private long id;
    private String name;
    private DishType type;
    private double totalCalories;
    private double totalCost;

    public DishDto(long id, String name, DishType type, double totalCalories, double totalCost) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.totalCalories = totalCalories;
        this.totalCost = totalCost;
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

    public double getTotalCalories() {
        return totalCalories;
    }

    public double getTotalCost() {
        return totalCost;
    }
}
