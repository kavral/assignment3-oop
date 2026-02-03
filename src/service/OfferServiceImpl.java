package service;

import model.FoodItem;
import model.Offer;
import repository.FoodItemRepository;
import repository.OfferRepository;
import exception.ValidationException;

import java.util.List;

public class OfferServiceImpl implements OfferService {

    private final OfferRepository offerRepository;
    private final FoodItemRepository foodItemRepository;

    public OfferServiceImpl(OfferRepository offerRepository, FoodItemRepository foodItemRepository) {
        this.offerRepository = offerRepository;
        this.foodItemRepository = foodItemRepository;
    }

    @Override
    public void addOffer(Offer offer) {
        if (!offer.validate()) {
            throw new ValidationException("Invalid offer data");
        }
        if (offer.getFoodItemId() <= 0) {
            throw new ValidationException("Food item ID must be positive");
        }

        FoodItem foodItem = foodItemRepository.findById(offer.getFoodItemId());
        if (foodItem == null) {
            throw new ValidationException("Food item not found with ID: " + offer.getFoodItemId());
        }

        offerRepository.save(offer);
    }

    @Override
    public List<Offer> getAllOffers() {
        return offerRepository.findAll();
    }

    @Override
    public List<Offer> getOffersByFoodItemId(int foodItemId) {
        if (foodItemId <= 0) {
            throw new ValidationException("Food item ID must be positive");
        }
        return offerRepository.findByFoodItemId(foodItemId);
    }

    @Override
    public List<Offer> getActiveOffers() {
        return offerRepository.findActiveOffers();
    }

    @Override
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

    @Override
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

    @Override
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

    @Override
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

    @Override
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
