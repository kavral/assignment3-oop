package model;

public interface PricedItem extends Validatable {

    double getPrice();

    default String getFormattedPrice() {
        return String.format("$%.2f", getPrice());
    }
}
