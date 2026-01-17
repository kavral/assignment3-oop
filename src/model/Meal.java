package model;

public class Meal extends FoodItem {

    public Meal(int id, String name, double price) {
        super(id, name, price);
    }

    @Override
    public double calculatePrice() {
        return price;
    }

    @Override
    public String getDescription() {
        return "Meal: " + name;
    }
}