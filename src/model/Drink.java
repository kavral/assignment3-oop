package model;

public class Drink extends FoodUnit {

    public Drink(int id, String name, double price) {
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