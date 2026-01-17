import model.*;
import service.FoodItemService;
import exception.FoodItemNotValidException;

public class Main {
    public static void main(String[] args) {

        FoodItemService service = new FoodItemService();

        FoodItem pizza = new Meal(1, "Pizza", 10.5);
        FoodItem badDrink = new Drink(2, "Cola", -3.0);

        service.createFoodItem(pizza);

        try {
            service.createFoodItem(badDrink);
        } catch (FoodItemNotValidException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }
}