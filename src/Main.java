import model.*;

public class Main {
    public static void main(String[] args) {

        FoodUnit burger = new Meal(1, "Burger", 8.99);
        FoodUnit cola = new Drink(2, "Cola", -2.0); // invalid price

        System.out.println(burger.getDescription());
        System.out.println("Valid: " + burger.validate());

        System.out.println(cola.getDescription());
        System.out.println("Valid: " + cola.validate());
    }
}