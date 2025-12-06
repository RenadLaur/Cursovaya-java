package kz.enu.restaurant.controller;

import kz.enu.restaurant.model.DishType;

import java.util.List;

public class DishDetailsDto {

    public static class IngredientLine {
        private String ingredientName;
        private double grams;
        private double calories;
        private double cost;

        public IngredientLine(String ingredientName, double grams, double calories, double cost) {
            this.ingredientName = ingredientName;
            this.grams = grams;
            this.calories = calories;
            this.cost = cost;
        }

        public String getIngredientName() {
            return ingredientName;
        }

        public double getGrams() {
            return grams;
        }

        public double getCalories() {
            return calories;
        }

        public double getCost() {
            return cost;
        }
    }

    private long id;
    private String name;
    private DishType type;
    private double totalCalories;
    private double totalCost;
    private List<IngredientLine> ingredients;

    public DishDetailsDto(long id,
                          String name,
                          DishType type,
                          double totalCalories,
                          double totalCost,
                          List<IngredientLine> ingredients) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.totalCalories = totalCalories;
        this.totalCost = totalCost;
        this.ingredients = ingredients;
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

    public List<IngredientLine> getIngredients() {
        return ingredients;
    }
}
