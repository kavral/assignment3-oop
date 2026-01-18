package service;

import model.Offer;
import repository.OfferRepository;
import repository.FoodItemRepository;
import exception.ValidationException;

import java.time.LocalDate;
import java.util.List;

public class OfferService {

    private final OfferRepository offerRepository = new OfferRepository();
    private final FoodItemRepository foodItemRepository = new FoodItemRepository();

    public void addOffer(Offer offer) {
        if (!offer.validate()) {
            throw new ValidationException("Invalid offer data");
        }

        if (offer.getFoodItemId() <= 0) {
            throw new ValidationException("Food item ID must be positive");
        }

        // Check if food item exists
        model.FoodItem foodItem = foodItemRepository.findById(offer.getFoodItemId());
        if (foodItem == null) {
            throw new ValidationException("Food item not found with ID: " + offer.getFoodItemId());
        }

        offerRepository.save(offer);
    }

    public List<Offer> getAllOffers() {
        return offerRepository.findAll();
    }

    public List<Offer> getOffersByFoodItemId(int foodItemId) {
        if (foodItemId <= 0) {
            throw new ValidationException("Food item ID must be positive");
        }
        return offerRepository.findByFoodItemId(foodItemId);
    }

    public List<Offer> getActiveOffers() {
        return offerRepository.findActiveOffers();
    }

    public Offer getOfferById(int id) {
        if (id <= 0) {
            throw new ValidationException("Offer ID must be positive");
        }
        Offer offer = offerRepository.findById(id);
        if (offer == null) {
            throw new ValidationException("Offer not found with ID: " + id);
        }
        return offer;
    }

    public void updateOffer(Offer offer) {
        if (offer.getId() <= 0) {
            throw new ValidationException("Offer ID must be positive");
        }
        if (!offer.validate()) {
            throw new ValidationException("Invalid offer data");
        }
        
        Offer existingOffer = offerRepository.findById(offer.getId());
        if (existingOffer == null) {
            throw new ValidationException("Offer not found with ID: " + offer.getId());
        }
        
        offerRepository.update(offer);
    }

    public void deleteOffer(int id) {
        if (id <= 0) {
            throw new ValidationException("Offer ID must be positive");
        }
        Offer offer = offerRepository.findById(id);
        if (offer == null) {
            throw new ValidationException("Offer not found with ID: " + id);
        }
        offerRepository.deleteById(id);
    }

    public void deactivateOffer(int id) {
        if (id <= 0) {
            throw new ValidationException("Offer ID must be positive");
        }
        Offer offer = offerRepository.findById(id);
        if (offer == null) {
            throw new ValidationException("Offer not found with ID: " + id);
        }
        offerRepository.deactivateById(id);
    }

    public double getDiscountedPrice(int foodItemId, double originalPrice) {
        List<Offer> activeOffers = offerRepository.findActiveOffers();
        
        for (Offer offer : activeOffers) {
            if (offer.getFoodItemId() == foodItemId && offer.isCurrentlyActive()) {
                return offer.calculateDiscountedPrice(originalPrice);
            }
        }
        
        return originalPrice;
    }
}
