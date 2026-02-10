package dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class OfferRequest {

    @Min(1)
    private int foodItemId;

    @Min(1)
    @Max(100)
    private double discountPercentage;

    private String description;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    public int getFoodItemId() {
        return foodItemId;
    }

    public void setFoodItemId(int foodItemId) {
        this.foodItemId = foodItemId;
    }

    public double getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}

