package service;

import cache.CacheKeys;
import cache.InMemoryCacheManager;
import exception.FoodItemNotValidException;
import model.FoodItem;
import model.Validatable;
import org.springframework.stereotype.Service;
import repository.FoodItemRepository;

import java.util.List;

@Service
public class FoodItemServiceImpl implements FoodItemService {

    private final FoodItemRepository repository;
    private final InMemoryCacheManager cache = InMemoryCacheManager.getInstance();

    public FoodItemServiceImpl(FoodItemRepository repository) {
        this.repository = repository;
    }

    @Override
    public FoodItem addFoodItem(FoodItem item) {
        if (!Validatable.isValid(item)) {
            throw new FoodItemNotValidException("Invalid food item: " + item.validationMessage());
        }
        FoodItem saved = repository.save(item);
        invalidateFoodItemsCache();
        return saved != null ? saved : null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<FoodItem> getAllFoodItems() {
        List<FoodItem> cached = (List<FoodItem>) cache.get(CacheKeys.FOOD_ITEMS_ALL);
        if (cached != null) {
            return cached;
        }
        List<FoodItem> list = repository.findAll();
        cache.put(CacheKeys.FOOD_ITEMS_ALL, list);
        return list;
    }

    private void invalidateFoodItemsCache() {
        cache.remove(CacheKeys.FOOD_ITEMS_ALL);
    }

    @Override
    public List<FoodItem> getAllFoodItemsSortedByName() {
        return repository.findAllSortedByName();
    }

    @Override
    public void updatePrice(String name, double price) {
        if (price <= 0) {
            throw new FoodItemNotValidException("Price must be positive");
        }
        repository.updatePrice(name, price);
        invalidateFoodItemsCache();
    }

    @Override
    public void deleteFoodItem(String name) {
        if (name == null || name.isBlank()) {
            throw new FoodItemNotValidException("Name cannot be empty");
        }
        repository.deleteByName(name);
        invalidateFoodItemsCache();
    }

    @Override
    public void deleteFoodItemById(int id) {
        if (id <= 0) {
            throw new FoodItemNotValidException("ID must be positive");
        }
        repository.deleteById(id);
        invalidateFoodItemsCache();
    }

    @Override
    public FoodItem getFoodItemById(int id) {
        if (id <= 0) {
            throw new FoodItemNotValidException("ID must be positive");
        }
        FoodItem item = repository.findById(id);
        if (item == null) {
            throw new FoodItemNotValidException("Food item not found with ID: " + id);
        }
        return item;
    }
}

