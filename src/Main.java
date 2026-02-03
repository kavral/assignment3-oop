import controller.FoodItemController;
import controller.OfferController;
import repository.FoodItemRepositoryImpl;
import repository.OfferRepositoryImpl;
import service.FoodItemServiceImpl;
import service.OfferServiceImpl;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        // Repository implementations (could be swapped for mock/other DB)
        var foodItemRepository = new FoodItemRepositoryImpl();
        var offerRepository = new OfferRepositoryImpl();

        // Services depend on repository interfaces (constructor injection)
        var foodItemService = new FoodItemServiceImpl(foodItemRepository);
        var offerService = new OfferServiceImpl(offerRepository, foodItemRepository);

        // Controllers depend on service interfaces (constructor injection)
        var foodItemController = new FoodItemController(foodItemService);
        var offerController = new OfferController(offerService, foodItemService);

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n=== MAIN MENU ===");
            System.out.println("1. Food Items Management");
            System.out.println("2. Offers Management");
            System.out.println("0. Exit");
            System.out.print("Choose: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> foodItemController.start();
                case 2 -> offerController.start();
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
