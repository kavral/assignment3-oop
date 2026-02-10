package service;

import model.FoodItem;

import java.util.List;

public interface FoodItemService {

    FoodItem addFoodItem(FoodItem item);

    List<FoodItem> getAllFoodItems();

    List<FoodItem> getAllFoodItemsSortedByName();

    void updatePrice(String name, double price);

    void deleteFoodItem(String name);

    void deleteFoodItemById(int id);

    FoodItem getFoodItemById(int id);
}

