package service;

import model.FoodItem;
import repository.FoodItemRepository;

import java.util.List;

public class FoodItemService {

    private final FoodItemRepository repository = new FoodItemRepository();

    public void addFoodItem(FoodItem item) {
        item.validate();
        repository.save(item);
    }

    public List<FoodItem> getAllFoodItems() {
        return repository.findAll();
    }

    public void updatePrice(String name, double price) {
        if (price <= 0) {
            throw new RuntimeException("Price must be positive");
        }
        repository.updatePrice(name, price);
    }

    public void deleteFoodItem(String name) {
        if (name == null || name.isBlank()) {
            throw new RuntimeException("Name cannot be empty");
        }
        repository.deleteByName(name);
    }
}