package model;

public class Drink extends FoodItem {

    private Integer volumeMl;

    public Drink(int id, String name, double price) {
        super(id, name, price);
    }

    public Drink(int id, String name, double price, Integer volumeMl) {
        super(id, name, price);
        this.volumeMl = volumeMl;
    }

    @Override
    public double calculatePrice() {
        return getPrice();
    }

    @Override
    public String getDescription() {
        return "Drink: " + getName() + (volumeMl != null ? " (" + volumeMl + " ml)" : "");
    }

    public Integer getVolumeMl() {
        return volumeMl;
    }

    public void setVolumeMl(Integer volumeMl) {
        this.volumeMl = volumeMl;
    }
}

