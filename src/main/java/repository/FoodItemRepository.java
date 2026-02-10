package repository;

import model.FoodItem;

import java.util.List;

public interface FoodItemRepository {

    FoodItem save(FoodItem item);

    FoodItem findById(int id);

    List<FoodItem> findAll();

    void deleteById(int id);

    List<FoodItem> findAllSortedByName();

    void updatePrice(String name, double newPrice);

    void deleteByName(String name);
}

