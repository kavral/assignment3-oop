package service;

import model.FoodItem;
import repository.FoodItemRepository;

import java.util.List;

public class FoodItemService {

    private final FoodItemRepository repository = new FoodItemRepository();

    public int addFoodItem(FoodItem item) {
        item.validate();
        return repository.save(item);
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

    public FoodItem getFoodItemById(int id) {
        if (id <= 0) {
            throw new RuntimeException("ID must be positive");
        }
        FoodItem item = repository.findById(id);
        if (item == null) {
            throw new RuntimeException("Food item not found with ID: " + id);
        }
        return item;
    }
}