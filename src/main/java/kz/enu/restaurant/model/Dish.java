package kz.enu.restaurant.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Dish extends MenuItem {

    private List<DishIngredient> ingredients = new ArrayList<>();

    public Dish(long id, String name, DishType type) {
        super(id, name, type);
    }

    public void addIngredient(DishIngredient di) {
        ingredients.add(di);
    }

    public List<DishIngredient> getIngredients() {
        return Collections.unmodifiableList(ingredients);
    }

    @Override
    public double getTotalCalories() {
        double sum = 0.0;
        for (DishIngredient di : ingredients) {
            sum += di.getIngredient().getCaloriesPer100g() * di.getGrams() / 100.0;
        }
        return sum;
    }

    @Override
    public double getTotalCost() {
        double sum = 0.0;
        for (DishIngredient di : ingredients) {
            sum += di.getIngredient().getPricePerKg() * di.getGrams() / 1000.0;
        }
        return sum;
    }
}
