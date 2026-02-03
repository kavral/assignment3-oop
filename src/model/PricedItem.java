package model;

/**
 * Narrow interface for priced entities (ISP).
 * Extends Validatable so anything with a price can be validated.
 */
public interface PricedItem extends Validatable {

    double getPrice();

    /** Default: formatted price string. */
    default String getFormattedPrice() {
        return String.format("$%.2f", getPrice());
    }
}
