package service;

import model.Offer;

import java.util.List;

public interface OfferService {

    void addOffer(Offer offer);

    List<Offer> getAllOffers();

    List<Offer> getOffersByFoodItemId(int foodItemId);

    List<Offer> getActiveOffers();

    Offer getOfferById(int id);

    void updateOffer(Offer offer);

    void deleteOffer(int id);

    void deactivateOffer(int id);

    double getDiscountedPrice(int foodItemId, double originalPrice);
}

