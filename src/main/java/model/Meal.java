package model;

public class Meal extends FoodItem {

    private Integer calories;

    public Meal(int id, String name, double price) {
        super(id, name, price);
    }

    public Meal(int id, String name, double price, Integer calories) {
        super(id, name, price);
        this.calories = calories;
    }

    @Override
    public double calculatePrice() {
        return getPrice();
    }

    @Override
    public String getDescription() {
        return "Meal: " + getName() + (calories != null ? " (" + calories + " cal)" : "");
    }

    public Integer getCalories() {
        return calories;
    }

    public void setCalories(Integer calories) {
        this.calories = calories;
    }
}

