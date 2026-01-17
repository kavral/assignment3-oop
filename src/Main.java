import model.FoodUnit;
import model.Meal;
import model.Drink;

public class Main {
    public static void main(String[] args) {

        FoodUnit item1 = new Meal(1, "Burger", 8.99);
        FoodUnit item2 = new Drink(2, "Cola", 2.49);

        System.out.println(item1.getDescription());
        System.out.println("Price: " + item1.calculatePrice());

        System.out.println(item2.getDescription());
        System.out.println("Price: " + item2.calculatePrice());
    }
}