package kz.enu.restaurant.model;

public class DishIngredient {

    private Ingredient ingredient;
    private double grams;

    public DishIngredient(Ingredient ingredient, double grams) {
        this.ingredient = ingredient;
        this.grams = grams;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public double getGrams() {
        return grams;
    }
}
