package controller;

import model.FoodItem;
import model.Meal;
import model.Drink;
import service.FoodItemService;

import java.util.List;
import java.util.Scanner;

public class FoodItemController {

    private final FoodItemService service = new FoodItemService();
    private final Scanner scanner = new Scanner(System.in);

    public void start() {

        boolean running = true;

        while (running) {
            System.out.println("\n=== FOOD DELIVERY PLATFORM ===");
            System.out.println("1. Add Meal");
            System.out.println("2. Add Drink");
            System.out.println("3. View All Items");
            System.out.println("4. Update Price");
            System.out.println("5. Delete Item");
            System.out.println("0. Exit");
            System.out.print("Choose: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            try {
                switch (choice) {
                    case 1 -> addMeal();
                    case 2 -> addDrink();
                    case 3 -> viewAll();
                    case 4 -> updatePrice();
                    case 5 -> deleteItem();
                    case 0 -> running = false;
                    default -> System.out.println("Invalid choice");
                }
            } catch (RuntimeException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void addMeal() {
        System.out.print("Meal name: ");
        String name = scanner.nextLine();

        if (name == null) {
            System.out.println("Name can not be empty");
            return;
        }

        System.out.print("Price: ");
        double price = scanner.nextDouble();
        scanner.nextLine();

        if (price < 0) {
            System.out.println("Price cannot be negative");
            return;
        }
        FoodItem meal = new Meal(0, name, price);
        int id = service.addFoodItem(meal);

        System.out.println("Meal added with ID: " + id);
    }

    private void addDrink() {
        System.out.print("Drink name: ");
        String name = scanner.nextLine();

        if (name == null) {
            System.out.println("Name can not be empty");
            return;
        }

        System.out.print("Price: ");
        double price = scanner.nextDouble();
        scanner.nextLine();

        if (price < 0) {
            System.out.println("Price cannot be negative");
            return;
        }

        FoodItem drink = new Drink(0, name, price);
        int id = service.addFoodItem(drink);

        System.out.println("Drink added with ID: " + id);
    }

    private void viewAll() {
        List<FoodItem> items = service.getAllFoodItems();

        System.out.println("\n--- MENU ---");
        for (FoodItem item : items) {
            System.out.println("ID: " + item.getId() + " | " + item.getDescription() + " | $" + item.getPrice());
        }
    }

    private void updatePrice() {
        System.out.print("Item name: ");
        String name = scanner.nextLine();

        System.out.print("New price: ");
        double price = scanner.nextDouble();
        scanner.nextLine();

        if (price < 0) {
            System.out.println("Price cannot be negative");
            return;
        }

        service.updatePrice(name, price);
        System.out.println("Price updated.");
    }

    private void deleteItem() {
        System.out.print("Item name: ");
        String name = scanner.nextLine();

        service.deleteFoodItem(name);
        System.out.println("Item deleted.");
    }
}