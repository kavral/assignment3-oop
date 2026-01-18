import controller.FoodItemController;
import controller.OfferController;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n=== MAIN MENU ===");
            System.out.println("1. Food Items Management");
            System.out.println("2. Offers Management");
            System.out.println("0. Exit");
            System.out.print("Choose: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1 -> new FoodItemController().start();
                case 2 -> new OfferController().start();
                case 0 -> {
                    running = false;
                    System.out.println("Goodbye!");
                }
                default -> System.out.println("Invalid choice");
            }
        }
        
        scanner.close();
    }
}