package dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class FoodItemRequest {

    @NotBlank
    private String name;

    @Min(0)
    private double price;

    @NotNull
    private FoodItemType type;

    public enum FoodItemType {
        MEAL,
        DRINK
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public FoodItemType getType() {
        return type;
    }

    public void setType(FoodItemType type) {
        this.type = type;
    }
}

