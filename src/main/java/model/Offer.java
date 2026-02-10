package model;

import java.time.LocalDate;

public class Offer {

    private int id;
    private int foodItemId;
    private double discountPercentage;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean isActive;

    public Offer(int id, int foodItemId, double discountPercentage, String description,
                 LocalDate startDate, LocalDate endDate, boolean isActive) {
        this.id = id;
        this.foodItemId = foodItemId;
        this.discountPercentage = discountPercentage;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isActive = isActive;
    }

    public Offer(int foodItemId, double discountPercentage, String description,
                 LocalDate startDate, LocalDate endDate) {
        this(0, foodItemId, discountPercentage, description, startDate, endDate, true);
    }

    public boolean validate() {
        if (discountPercentage <= 0 || discountPercentage > 100) return false;
        if (startDate == null || endDate == null) return false;
        if (endDate.isBefore(startDate)) return false;
        return foodItemId > 0;
    }

    public double calculateDiscountedPrice(double originalPrice) {
        return originalPrice * (1 - discountPercentage / 100);
    }

    public boolean isCurrentlyActive() {
        LocalDate today = LocalDate.now();
        return isActive && !today.isBefore(startDate) && !today.isAfter(endDate);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}

