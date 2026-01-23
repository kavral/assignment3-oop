package controller;

import model.Offer;
import service.OfferService;
import service.FoodItemService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class OfferController {

    private final OfferService offerService = new OfferService();
    private final FoodItemService foodItemService = new FoodItemService();
    private final Scanner scanner = new Scanner(System.in);
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public void start() {
        boolean running = true;

        while (running) {
            System.out.println("\n=== OFFER MANAGEMENT ===");
            System.out.println("1. Add Offer");
            System.out.println("2. View All Offers");
            System.out.println("3. View Active Offers");
            System.out.println("4. View Offers by Food Item");
            System.out.println("5. Update Offer");
            System.out.println("6. Deactivate Offer");
            System.out.println("7. Delete Offer");
            System.out.println("0. Back to Main Menu");
            System.out.print("Choose: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            try {
                switch (choice) {
                    case 1 -> addOffer();
                    case 2 -> viewAllOffers();
                    case 3 -> viewActiveOffers();
                    case 4 -> viewOffersByFoodItem();
                    case 5 -> updateOffer();
                    case 6 -> deactivateOffer();
                    case 7 -> deleteOffer();
                    case 0 -> running = false;
                    default -> System.out.println("Invalid choice");
                }
            } catch (RuntimeException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void addOffer() {
        System.out.println("\n--- Add New Offer ---");
        
        // Show available food items
        System.out.println("\nAvailable Food Items:");
        foodItemService.getAllFoodItems().forEach(item -> 
            System.out.println("ID: " + item.getId() + " - " + item.getName() + " - $" + item.getPrice())
        );
        
        System.out.print("Food Item ID: ");
        int foodItemId = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Discount Percentage (0-100): ");
        double discountPercentage = scanner.nextDouble();
        scanner.nextLine();

        if (discountPercentage < 0 || discountPercentage > 100) {
            System.out.println("From 0 to 100 only!");
            return;
        }

        System.out.print("Description (optional): ");
        String description = scanner.nextLine();

        System.out.print("Start Date (yyyy-MM-dd): ");
        String startDateStr = scanner.nextLine();
        LocalDate startDate = parseDate(startDateStr);

        System.out.print("End Date (yyyy-MM-dd): ");
        String endDateStr = scanner.nextLine();
        LocalDate endDate = parseDate(endDateStr);

        Offer offer = new Offer(foodItemId, discountPercentage, description, startDate, endDate);
        offerService.addOffer(offer);
        System.out.println("Offer added successfully!");
    }

    private void viewAllOffers() {
        List<Offer> offers = offerService.getAllOffers();
        
        System.out.println("\n--- All Offers ---");
        if (offers.isEmpty()) {
            System.out.println("No offers found.");
        } else {
            offers.forEach(offer -> {
                System.out.println(offer.toString());
                System.out.println("  Status: " + (offer.isActive() ? "Active" : "Inactive"));
            });
        }
    }

    private void viewActiveOffers() {
        List<Offer> offers = offerService.getActiveOffers();
        
        System.out.println("\n--- Active Offers ---");
        if (offers.isEmpty()) {
            System.out.println("No active offers found.");
        } else {
            offers.forEach(offer -> 
                System.out.println(offer.toString())
            );
        }
    }

    private void viewOffersByFoodItem() {
        System.out.print("Food Item ID: ");
        int foodItemId = scanner.nextInt();
        scanner.nextLine();

        List<Offer> offers = offerService.getOffersByFoodItemId(foodItemId);
        
        System.out.println("\n--- Offers for Food Item ID " + foodItemId + " ---");
        if (offers.isEmpty()) {
            System.out.println("No offers found for this food item.");
        } else {
            offers.forEach(offer -> 
                System.out.println(offer.toString())
            );
        }
    }

    private void updateOffer() {
        System.out.print("Offer ID to update: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        Offer offer = offerService.getOfferById(id);
        
        System.out.println("Current offer: " + offer.toString());
        System.out.println("\nEnter new values (press Enter to keep current value):");

        System.out.print("Food Item ID [" + offer.getFoodItemId() + "]: ");
        String foodItemIdStr = scanner.nextLine();
        if (!foodItemIdStr.isEmpty()) {
            offer.setFoodItemId(Integer.parseInt(foodItemIdStr));
        }

        System.out.print("Discount Percentage [" + offer.getDiscountPercentage() + "]: ");
        String discountStr = scanner.nextLine();
        if (!discountStr.isEmpty()) {
            offer.setDiscountPercentage(Double.parseDouble(discountStr));
        }

        System.out.print("Description [" + offer.getDescription() + "]: ");
        String description = scanner.nextLine();
        if (!description.isEmpty()) {
            offer.setDescription(description);
        }

        System.out.print("Start Date [" + offer.getStartDate() + "] (yyyy-MM-dd): ");
        String startDateStr = scanner.nextLine();
        if (!startDateStr.isEmpty()) {
            offer.setStartDate(parseDate(startDateStr));
        }

        System.out.print("End Date [" + offer.getEndDate() + "] (yyyy-MM-dd): ");
        String endDateStr = scanner.nextLine();
        if (!endDateStr.isEmpty()) {
            offer.setEndDate(parseDate(endDateStr));
        }

        offerService.updateOffer(offer);
        System.out.println("Offer updated successfully!");
    }

    private void deactivateOffer() {
        System.out.print("Offer ID to deactivate: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        offerService.deactivateOffer(id);
        System.out.println("Offer deactivated successfully!");
    }

    private void deleteOffer() {
        System.out.print("Offer ID to delete: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Are you sure you want to delete this offer? (yes/no): ");
        String confirmation = scanner.nextLine();
        
        if ("yes".equalsIgnoreCase(confirmation)) {
            offerService.deleteOffer(id);
            System.out.println("Offer deleted successfully!");
        } else {
            System.out.println("Deletion cancelled.");
        }
    }

    private LocalDate parseDate(String dateStr) {
        try {
            return LocalDate.parse(dateStr, dateFormatter);
        } catch (DateTimeParseException e) {
            throw new RuntimeException("Invalid date format. Please use yyyy-MM-dd");
        }
    }
}
