package kz.enu.restaurant.model;

public class Beverage extends MenuItem {

    private double volumeMl;
    private double caloriesPer100Ml;
    private double pricePerLiter;

    public Beverage(long id,
                    String name,
                    DishType type,
                    double volumeMl,
                    double caloriesPer100Ml,
                    double pricePerLiter) {
        super(id, name, type);
        this.volumeMl = volumeMl;
        this.caloriesPer100Ml = caloriesPer100Ml;
        this.pricePerLiter = pricePerLiter;
    }

    public double getVolumeMl() {
        return volumeMl;
    }

    @Override
    public double getTotalCalories() {
        return caloriesPer100Ml * volumeMl / 100.0;
    }

    @Override
    public double getTotalCost() {
        return pricePerLiter * volumeMl / 1000.0;
    }
}
