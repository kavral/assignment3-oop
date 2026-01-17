package service;

import model.FoodItem;
import exception.FoodItemNotValidException;

public class FoodItemService {

    public void createFoodItem(FoodItem item) {
        if (!item.validate()) {
            throw new FoodItemNotValidException(
                    "Food item validation failed: " + item.getName()
            );
        }

        System.out.println("Food item created: " + item.basicInfo());
    }
}