package patterns.factory;

import dto.FoodItemRequest;
import logging.LoggerService;
import model.Drink;
import model.FoodItem;
import model.Meal;

/**
 * Factory for creating FoodItem subclasses based on DTO input.
 */
public class FoodItemFactory {

    private final LoggerService logger = LoggerService.getInstance();

    public FoodItem create(FoodItemRequest request) {
        return switch (request.getType()) {
            case MEAL -> {
                logger.info("Creating Meal from REST request");
                yield new Meal(0, request.getName(), request.getPrice());
            }
            case DRINK -> {
                logger.info("Creating Drink from REST request");
                yield new Drink(0, request.getName(), request.getPrice());
            }
        };
    }
}

