package repository;

import model.Offer;

import java.util.List;

public interface OfferRepository {

    void save(Offer offer);

    Offer findById(int id);

    List<Offer> findAll();

    void update(Offer offer);

    void deleteById(int id);

    void deactivateById(int id);

    List<Offer> findByFoodItemId(int foodItemId);

    List<Offer> findActiveOffers();
}

