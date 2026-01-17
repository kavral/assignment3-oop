package model;

public abstract class FoodUnit implements PricedUnit, Validatable {

    protected int id;
    protected String name;
    protected double price;

    public FoodUnit(int id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public abstract double calculatePrice();
    public abstract String getDescription();

    @Override
    public double getPrice() {
        return price;
    }

    @Override
    public boolean validate() {
        return price > 0 && name != null && !name.isEmpty();
    }

    public String basicInfo() {
        return name + " - $" + price;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}