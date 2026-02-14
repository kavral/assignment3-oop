package patterns.builder;

import model.Offer;

import java.time.LocalDate;

public class OfferBuilder {

    private int id;
    private int foodItemId;
    private double discountPercentage;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean active = true;

    public OfferBuilder id(int id) {
        this.id = id;
        return this;
    }

    public OfferBuilder foodItemId(int foodItemId) {
        this.foodItemId = foodItemId;
        return this;
    }

    public OfferBuilder discountPercentage(double discountPercentage) {
        this.discountPercentage = discountPercentage;
        return this;
    }

    public OfferBuilder description(String description) {
        this.description = description;
        return this;
    }

    public OfferBuilder startDate(LocalDate startDate) {
        this.startDate = startDate;
        return this;
    }

    public OfferBuilder endDate(LocalDate endDate) {
        this.endDate = endDate;
        return this;
    }

    public OfferBuilder active(boolean active) {
        this.active = active;
        return this;
    }

    public Offer build() {
        return new Offer(id, foodItemId, discountPercentage, description, startDate, endDate, active);
    }

    public static OfferBuilder fromExisting(Offer existing) {
        return new OfferBuilder()
                .id(existing.getId())
                .foodItemId(existing.getFoodItemId())
                .discountPercentage(existing.getDiscountPercentage())
                .description(existing.getDescription())
                .startDate(existing.getStartDate())
                .endDate(existing.getEndDate())
                .active(existing.isActive());
    }
}

