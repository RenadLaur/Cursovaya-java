package kz.enu.restaurant.model;

public class Ingredient {

    private long id;
    private String name;
    private double caloriesPer100g;
    private double pricePerKg;

    public Ingredient(long id, String name, double caloriesPer100g, double pricePerKg) {
        this.id = id;
        this.name = name;
        this.caloriesPer100g = caloriesPer100g;
        this.pricePerKg = pricePerKg;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getCaloriesPer100g() {
        return caloriesPer100g;
    }

    public double getPricePerKg() {
        return pricePerKg;
    }
}
