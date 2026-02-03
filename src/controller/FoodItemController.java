package controller;

import model.FoodItem;
import model.Meal;
import model.Drink;
import service.FoodItemService;
import utils.ReflectionUtil;

import java.util.List;
import java.util.Scanner;

public class FoodItemController {

    private final FoodItemService service;
    private final Scanner scanner = new Scanner(System.in);

    public FoodItemController(FoodItemService service) {
        this.service = service;
    }

    public void start() {
        boolean running = true;

        while (running) {
            System.out.println("\n=== FOOD DELIVERY PLATFORM ===");
            System.out.println("1. Add Meal");
            System.out.println("2. Add Drink");
            System.out.println("3. View All Items");
            System.out.println("4. View All Items (Sorted by Name - Lambda)");
            System.out.println("5. Update Price");
            System.out.println("6. Delete Item by Name");
            System.out.println("7. Delete Item by ID");
            System.out.println("8. Reflection Demo (inspect entity)");
            System.out.println("0. Exit");
            System.out.print("Choose: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            try {
                switch (choice) {
                    case 1 -> addMeal();
                    case 2 -> addDrink();
                    case 3 -> viewAll();
                    case 4 -> viewAllSortedByName();
                    case 5 -> updatePrice();
                    case 6 -> deleteItemByName();
                    case 7 -> deleteItemById();
                    case 8 -> reflectionDemo();
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

        if (name == null || name.isEmpty()) {
            System.out.println("Name cannot be empty");
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
        FoodItem saved = service.addFoodItem(meal);
        if (saved != null) {
            System.out.println("Meal added with ID: " + saved.getId());
        } else {
            System.out.println("Failed to add meal.");
        }
    }

    private void addDrink() {
        System.out.print("Drink name: ");
        String name = scanner.nextLine();

        if (name == null || name.isEmpty()) {
            System.out.println("Name cannot be empty");
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
        FoodItem saved = service.addFoodItem(drink);
        if (saved != null) {
            System.out.println("Drink added with ID: " + saved.getId());
        } else {
            System.out.println("Failed to add drink.");
        }
    }

    private void viewAll() {
        List<FoodItem> items = service.getAllFoodItems();
        System.out.println("\n--- MENU ---");
        for (FoodItem item : items) {
            System.out.println("ID: " + item.getId() + " | " + item.getDescription() + " | " + item.getFormattedPrice());
        }
    }

    private void viewAllSortedByName() {
        List<FoodItem> items = service.getAllFoodItemsSortedByName();
        System.out.println("\n--- MENU (Sorted by Name) ---");
        items.forEach(item ->
                System.out.println("ID: " + item.getId() + " | " + item.getDescription() + " | " + item.getFormattedPrice())
        );
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

    private void deleteItemByName() {
        System.out.print("Item name: ");
        String name = scanner.nextLine();

        service.deleteFoodItem(name);
        System.out.println("Item deleted.");
    }

    private void deleteItemById() {
        System.out.print("Item ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        service.deleteFoodItemById(id);
        System.out.println("Item deleted.");
    }

    private void reflectionDemo() {
        FoodItem sample = new Meal(1, "Sample Meal", 9.99);
        System.out.println(ReflectionUtil.describe(sample));
    }
}
