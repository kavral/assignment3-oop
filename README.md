# Online Food Delivery Platform

Java OOP + JDBC project for managing food items and special offers.

---

## A. Project Overview

### Purpose of API
This is a console-based food delivery management system built with Java, utilizing Object-Oriented Programming principles and JDBC for database connectivity. The system allows users to:
- Manage food items (Meals and Drinks) with CRUD operations
- Create and manage special offers with discount percentages
- Track active offers and their validity periods
- Maintain relationships between food items and their offers

### Summary of Entities and Their Relationships
The application consists of two main entities:

1. **FoodItem** (Abstract Base Class)
   - Subclasses: `Meal` and `Drink`
   - Attributes: `id`, `name`, `price`, `type`
   - Represents menu items in the delivery platform

2. **Offer**
   - Attributes: `id`, `foodItemId`, `discountPercentage`, `description`, `startDate`, `endDate`, `isActive`
   - **Relationship**: One-to-Many (One FoodItem can have multiple Offers)
   - Connected via foreign key: `food_item_id` references `food_items(id)`

### OOP Design Overview
The application follows a **multi-layer architecture**:
- **Model Layer**: Domain entities (`FoodItem`, `Meal`, `Drink`, `Offer`)
- **Repository Layer**: Data access objects (`FoodItemRepository`, `OfferRepository`)
- **Service Layer**: Business logic (`FoodItemService`, `OfferService`)
- **Controller Layer**: User interface (`FoodItemController`, `OfferController`)
- **Utils Layer**: Utility classes (`DatabaseConnection`)

This separation ensures:
- **Single Responsibility**: Each layer has a specific purpose
- **Loose Coupling**: Changes in one layer don't affect others
- **Maintainability**: Easy to modify and extend

---

## B. OOP Design Documentation

### Abstract Class and Subclasses

#### Abstract Class: `FoodItem`
```java
public abstract class FoodItem implements PricedItem, Validatable {
    protected int id;
    protected String name;
    protected double price;
    
    // Abstract methods (must be implemented by subclasses)
    public abstract double calculatePrice();
    public abstract String getDescription();
    
    // Concrete methods
    public double getPrice() { return price; }
    public boolean validate() { return price > 0 && name != null && !name.isEmpty(); }
}
```

#### Subclasses:
1. **`Meal`** extends `FoodItem`
   - Implements `calculatePrice()` to return the base price
   - Implements `getDescription()` to return "Meal: {name}"

2. **`Drink`** extends `FoodItem`
   - Implements `calculatePrice()` to return the base price
   - Implements `getDescription()` to return "Drink: {name}"

**Benefits of Abstract Class:**
- Ensures all food items share common attributes and behaviors
- Forces subclasses to implement specific methods
- Prevents direct instantiation of `FoodItem` (must use `Meal` or `Drink`)

### Interfaces and Implemented Methods

#### Interface: `PricedItem`
```java
public interface PricedItem {
    double getPrice();
}
```
- **Implemented by**: `FoodItem` (and all its subclasses)
- **Purpose**: Ensures all items can provide their price information
- **Polymorphism**: Can treat any `FoodItem` as a `PricedItem`

#### Interface: `Validatable`
```java
public interface Validatable {
    boolean validate();
}
```
- **Implemented by**: `FoodItem` (and all its subclasses)
- **Purpose**: Ensures all items can validate their data integrity
- **Usage**: Called before saving to database

### Composition/Aggregation

**Repository Composition:**
- `FoodItemRepository` uses `DatabaseConnection` (composition)
- `OfferRepository` uses `DatabaseConnection` (composition)
- Services compose repositories:
  - `FoodItemService` contains `FoodItemRepository`
  - `OfferService` contains `OfferRepository` and `FoodItemRepository`
- Controllers compose services:
  - `FoodItemController` contains `FoodItemService`
  - `OfferController` contains `OfferService` and `FoodItemService`

**Aggregation:**
- `Offer` aggregates information about `FoodItem` via `foodItemId` (not direct object reference, but logical aggregation)

### Polymorphism Examples

#### Example 1: Method Overriding
```java
// In FoodItem (abstract)
public abstract String getDescription();

// In Meal
@Override
public String getDescription() {
    return "Meal: " + name;
}

// In Drink
@Override
public String getDescription() {
    return "Drink: " + name;
}
```

#### Example 2: Interface Polymorphism
```java
// FoodItem implements PricedItem
List<FoodItem> items = service.getAllFoodItems();
for (FoodItem item : items) {
    // Can call getPrice() because FoodItem implements PricedItem
    double price = item.getPrice();
    
    // Polymorphism: same method call, different behavior based on actual type
    String desc = item.getDescription(); // "Meal: Pizza" or "Drink: Cola"
}
```

#### Example 3: Runtime Polymorphism
```java
// The actual type is determined at runtime
FoodItem item1 = new Meal(1, "Pizza", 15.99);
FoodItem item2 = new Drink(2, "Cola", 2.50);

// Same reference type, different behaviors
System.out.println(item1.getDescription()); // "Meal: Pizza"
System.out.println(item2.getDescription()); // "Drink: Cola"
```

### UML Diagram

<img width="987" height="436" alt="image" src="https://github.com/user-attachments/assets/f1e56c75-3573-4d62-a735-8244e5842fce" />

## C. Database Description

### Schema

#### Table: `food_items`
```sql
CREATE TABLE food_items (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255),
    price DECIMAL(10, 2),
    type VARCHAR(50)
);
```

**Columns:**
- `id`: Primary key, auto-incrementing integer
- `name`: Food item name (e.g., "Pizza Margherita")
- `price`: Item price (decimal with 2 decimal places)
- `type`: Item type ("Meal" or "Drink")

**Constraints:**
- Primary key on `id`
- No explicit NOT NULL constraints (as per user's schema)

#### Table: `offers`
```sql
CREATE TABLE offers (
    id SERIAL PRIMARY KEY,
    food_item_id INT,
    discount_percentage DECIMAL(5, 2),
    description VARCHAR(500),
    start_date DATE,
    end_date DATE,
    is_active BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (food_item_id) REFERENCES food_items(id) ON DELETE CASCADE
);
```

**Columns:**
- `id`: Primary key, auto-incrementing integer
- `food_item_id`: Foreign key referencing `food_items(id)`
- `discount_percentage`: Discount percentage (0-100)
- `description`: Optional offer description
- `start_date`: Offer start date
- `end_date`: Offer end date
- `is_active`: Boolean flag (default: TRUE)

**Constraints:**
- Primary key on `id`
- Foreign key constraint: `food_item_id` → `food_items(id)`
- **ON DELETE CASCADE**: When a food item is deleted, all its offers are automatically deleted

**Relationships:**
- **One-to-Many**: One `food_item` can have multiple `offers`
- **Referential Integrity**: Ensures offers can only reference existing food items

### Sample SQL Inserts

#### Insert Food Items:
```sql
-- Insert a meal
INSERT INTO food_items (name, price, type) 
VALUES ('Pizza Margherita', 15.99, 'Meal');

-- Insert a drink
INSERT INTO food_items (name, price, type) 
VALUES ('Coca Cola', 2.50, 'Drink');

-- Insert more meals
INSERT INTO food_items (name, price, type) 
VALUES ('Burger Deluxe', 12.99, 'Meal');

INSERT INTO food_items (name, price, type) 
VALUES ('Caesar Salad', 10.99, 'Meal');
```

#### Insert Offers:
```sql
-- 20% off on Pizza (valid for one week)
INSERT INTO offers (food_item_id, discount_percentage, description, start_date, end_date, is_active)
VALUES (1, 20.00, 'Weekend Special - 20% off on all pizzas', '2024-01-01', '2024-01-07', TRUE);

-- 15% off on Burger (monthly promotion)
INSERT INTO offers (food_item_id, discount_percentage, description, start_date, end_date, is_active)
VALUES (3, 15.00, 'Monthly Burger Special', '2024-01-01', '2024-01-31', TRUE);

-- 10% off on Drinks
INSERT INTO offers (food_item_id, discount_percentage, description, start_date, end_date, is_active)
VALUES (2, 10.00, 'Happy Hour - 10% off drinks', '2024-01-01', '2024-12-31', TRUE);
```

---

## D. Controller

The application uses **Controller classes** to handle user interaction through a console-based menu system.

### FoodItemController - CRUD Operations

#### 1. **CREATE** - Add Food Item
**Menu Option:** `1. Add Meal` or `2. Add Drink`

**Example Flow:**
```
=== FOOD DELIVERY PLATFORM ===
1. Add Meal
2. Add Drink
...
Choose: 1

Meal name: Pizza Margherita
Price: 15.99
Meal added with ID: 1
```

**Request/Response:**
- **Input**: Name (String), Price (double)
- **Action**: Creates `Meal` or `Drink` object, validates, saves to database
- **Output**: Confirmation message with generated ID

#### 2. **READ** - View All Items
**Menu Option:** `3. View All Items`

**Example Flow:**
```
Choose: 3

--- MENU ---
ID: 1 | Meal: Pizza Margherita | $15.99
ID: 2 | Drink: Coca Cola | $2.50
ID: 3 | Meal: Burger Deluxe | $12.99
```

**Request/Response:**
- **Input**: None
- **Action**: Retrieves all food items from database
- **Output**: List of items with ID, description, and price

#### 3. **UPDATE** - Update Price
**Menu Option:** `4. Update Price`

**Example Flow:**
```
Choose: 4

Item name: Pizza Margherita
New price: 14.99
Price updated.
```

**Request/Response:**
- **Input**: Item name (String), New price (double)
- **Action**: Updates price in database
- **Output**: Confirmation message
- **Error Handling**: If price ≤ 0, throws exception

#### 4. **DELETE** - Delete Item
**Menu Option:** `5. Delete Item`

**Example Flow:**
```
Choose: 5

Item name: Pizza Margherita
Item deleted.
```

**Request/Response:**
- **Input**: Item name (String)
- **Action**: Deletes item from database (cascades to offers)
- **Output**: Confirmation message
- **Error Handling**: If name is empty, throws exception

### OfferController - CRUD Operations

#### 1. **CREATE** - Add Offer
**Menu Option:** `1. Add Offer`

**Example Flow:**
```
=== OFFER MANAGEMENT ===
1. Add Offer
...
Choose: 1

--- Add New Offer ---

Available Food Items:
ID: 1 - Pizza Margherita - $15.99
ID: 2 - Coca Cola - $2.50
ID: 3 - Burger Deluxe - $12.99

Food Item ID: 1
Discount Percentage (0-100): 20
Description (optional): Weekend Special
Start Date (yyyy-MM-dd): 2024-01-01
End Date (yyyy-MM-dd): 2024-01-07
Offer added successfully!
```

**Request/Response:**
- **Input**: Food Item ID, Discount %, Description, Start Date, End Date
- **Action**: Validates offer, checks food item exists, saves to database
- **Output**: Success message
- **Error Handling**: 
  - Invalid discount percentage (≤0 or >100)
  - Invalid date range (end before start)
  - Food item not found

#### 2. **READ** - View Offers
**Menu Options:** 
- `2. View All Offers`
- `3. View Active Offers`
- `4. View Offers by Food Item`

**Example Flow (View All):**
```
Choose: 2

--- All Offers ---
Offer #1 - 20.00% off (Valid: 2024-01-01 to 2024-01-07) - Weekend Special
  Status: Active
Offer #2 - 15.00% off (Valid: 2024-01-01 to 2024-01-31) - Monthly Burger Special
  Status: Active
```

**Example Flow (View Active):**
```
Choose: 3

--- Active Offers ---
Offer #1 - 20.00% off (Valid: 2024-01-01 to 2024-01-07) - Weekend Special
```

**Example Flow (By Food Item):**
```
Choose: 4

Food Item ID: 1

--- Offers for Food Item ID 1 ---
Offer #1 - 20.00% off (Valid: 2024-01-01 to 2024-01-07) - Weekend Special
```

#### 3. **UPDATE** - Update Offer
**Menu Option:** `5. Update Offer`

**Example Flow:**
```
Choose: 5

Offer ID to update: 1
Current offer: Offer #1 - 20.00% off (Valid: 2024-01-01 to 2024-01-07) - Weekend Special

Enter new values (press Enter to keep current value):
Food Item ID [1]: 
Discount Percentage [20.00]: 25
Description [Weekend Special]: Extended Weekend Special
Start Date [2024-01-01] (yyyy-MM-dd): 
End Date [2024-01-07]: 2024-01-14
Offer updated successfully!
```

**Request/Response:**
- **Input**: Offer ID, new values (optional - press Enter to keep current)
- **Action**: Updates offer in database
- **Output**: Success message
- **Error Handling**: Invalid data, offer not found

#### 4. **UPDATE** - Deactivate Offer
**Menu Option:** `6. Deactivate Offer`

**Example Flow:**
```
Choose: 6

Offer ID to deactivate: 1
Offer deactivated successfully!
```

#### 5. **DELETE** - Delete Offer
**Menu Option:** `7. Delete Offer`

**Example Flow:**
```
Choose: 7

Offer ID to delete: 1
Are you sure you want to delete this offer? (yes/no): yes
Offer deleted successfully!
```

**Request/Response:**
- **Input**: Offer ID, confirmation
- **Action**: Permanently deletes offer from database
- **Output**: Success message
- **Error Handling**: Offer not found

### Error Handling Examples

**Validation Error:**
```
Meal name: 
Price: -5
Error: Price must be positive
```

**Not Found Error:**
```
Offer ID to update: 999
Error: Offer not found with ID: 999
```

**Date Format Error:**
```
Start Date (yyyy-MM-dd): 2024/01/01
Error: Invalid date format. Please use yyyy-MM-dd
```

---

## E. Instructions to Compile and Run

### Prerequisites
1. **Java Development Kit (JDK)** 8 or higher
2. **PostgreSQL Database** (installed and running)
3. **PostgreSQL JDBC Driver** (postgresql-XX.X.X.jar)

### Database Setup

1. **Start PostgreSQL service**
   ```bash
   # Windows (as Administrator)
   net start postgresql-x64-XX
   
   # Or using pg_ctl
   pg_ctl start -D "C:\Program Files\PostgreSQL\XX\data"
   ```

2. **Create database**
   ```sql
   CREATE DATABASE food_delivery;
   ```

3. **Run schema script**
   ```bash
   psql -U postgres -d food_delivery -f resources/sheme.sql
   ```
   Or using pgAdmin: Open SQL script and execute

4. **Update database connection** (if needed)
   Edit `src/utils/DatabaseConnection.java`:
   ```java
   private static final String URL = "jdbc:postgresql://localhost:5432/food_delivery";
   private static final String USER = "postgres";
   private static final String PASSWORD = "your_password";
   ```

### Compilation

#### Method 1: Using javac (Command Line)

1. **Navigate to project root**
   ```bash
   cd C:\AITU-files\assignment3-oop
   ```

2. **Set CLASSPATH to include PostgreSQL JDBC driver**
   ```bash
   # Download postgresql-XX.X.X.jar and place it in a lib folder
   set CLASSPATH=lib\postgresql-XX.X.X.jar;.
   ```

3. **Compile all Java files**
   ```bash
   javac -d out/production/assignment3-oop -cp %CLASSPATH% src\**\*.java
   ```
   
   Or compile individually:
   ```bash
   javac -d out/production/assignment3-oop -cp lib\postgresql-XX.X.X.jar src\Main.java src\model\*.java src\controller\*.java src\service\*.java src\repository\*.java src\utils\*.java src\exception\*.java
   ```

#### Method 2: Using IntelliJ IDEA

1. **Open project** in IntelliJ IDEA
2. **Add JDBC driver**:
   - File → Project Structure → Libraries
   - Click "+" → From Maven
   - Search: `org.postgresql:postgresql:42.7.1`
   - Or download JAR and add manually
3. **Build project**:
   - Build → Build Project (Ctrl+F9)
   - Or Build → Rebuild Project

### Running the Application

#### Method 1: Using java command

```bash
# Navigate to output directory
cd out\production\assignment3-oop

# Run with CLASSPATH
java -cp "lib\postgresql-XX.X.X.jar;." Main
```

#### Method 2: Using IntelliJ IDEA

1. **Right-click** on `Main.java`
2. Select **Run 'Main.main()'**
3. Or press **Shift+F10**

#### Method 3: Using JAR file (Advanced)

1. **Create manifest file** (`MANIFEST.MF`):
   ```
   Main-Class: Main
   Class-Path: lib/postgresql-XX.X.X.jar
   ```

2. **Create JAR**:
   ```bash
   jar cvfm app.jar MANIFEST.MF -C out/production/assignment3-oop .
   ```

3. **Run JAR**:
   ```bash
   java -jar app.jar
   ```

### Example Execution Flow

```
=== MAIN MENU ===
1. Food Items Management
2. Offers Management
0. Exit
Choose: 1

=== FOOD DELIVERY PLATFORM ===
1. Add Meal
2. Add Drink
3. View All Items
4. Update Price
5. Delete Item
0. Exit
Choose: 1

Meal name: Pizza Margherita
Price: 15.99
Meal added with ID: 1

... (continue using the application)
```

---

## F. Screenshots Instructions

To demonstrate successful CRUD operations and error handling, take screenshots of the following scenarios:

### Required Screenshots:

#### 1. **Database Setup**
- Screenshot: PostgreSQL database with both `food_items` and `offers` tables created
- **How to capture:**
  - Open pgAdmin or psql
  - Run `\dt` command or expand tables in pgAdmin
  - Take screenshot showing both tables exist

#### 2. **CREATE Operations**
   - **2a. Adding a Meal:**
     - Screenshot: Menu selection → Enter meal details → Success message
     - **Steps:**
       1. Run the application
       2. Select option 1 (Food Items Management)
       3. Select option 1 (Add Meal)
       4. Enter: "Pizza Margherita", price: 15.99
       5. Capture the success message showing ID
   
   - **2b. Adding an Offer:**
     - Screenshot: Menu selection → View available items → Enter offer details → Success
     - **Steps:**
       1. Select option 2 (Offers Management)
       2. Select option 1 (Add Offer)
       3. Capture the list of available food items
       4. Enter offer details (discount, dates, description)
       5. Capture success message

#### 3. **READ Operations**
   - **3a. View All Food Items:**
     - Screenshot: List of all items with IDs
     - **Steps:**
       1. Select option 3 (View All Items)
       2. Capture the complete list showing IDs, names, prices
   
   - **3b. View All Offers:**
     - Screenshot: List of all offers
     - **Steps:**
       1. Select option 2 (View All Offers)
       2. Capture the list showing all offer details
   
   - **3c. View Active Offers:**
     - Screenshot: Only currently active offers
     - **Steps:**
       1. Select option 3 (View Active Offers)
       2. Capture filtered list

#### 4. **UPDATE Operations**
   - **4a. Update Food Item Price:**
     - Screenshot: Before update → Update command → After update (view all)
     - **Steps:**
       1. View all items (note current price)
       2. Select option 4 (Update Price)
       3. Enter item name and new price
       4. View all items again to show updated price
   
   - **4b. Update Offer:**
     - Screenshot: Current offer → Update form → Updated offer
     - **Steps:**
       1. View offer details
       2. Select option 5 (Update Offer)
       3. Enter new values
       4. View updated offer

#### 5. **DELETE Operations**
   - **5a. Delete Offer:**
     - Screenshot: List with offer → Delete confirmation → List without offer
     - **Steps:**
       1. View all offers (note ID of one to delete)
       2. Select option 7 (Delete Offer)
       3. Enter ID and confirm
       4. View all offers again (should be gone)
   
   - **5b. Delete Food Item (with cascade):**
     - Screenshot: Item with offers → Delete item → Verify offers also deleted
     - **Steps:**
       1. Create a food item
       2. Create offers for that item
       3. View offers by food item ID
       4. Delete the food item
       5. Try to view offers for that item (should show empty)

#### 6. **Error Handling**
   - **6a. Validation Error - Invalid Price:**
     - Screenshot: Enter negative price → Error message
     - **Steps:**
       1. Try to add meal with price -5
       2. Capture error message
   
   - **6b. Validation Error - Invalid Discount:**
     - Screenshot: Enter discount > 100 → Error message
     - **Steps:**
       1. Try to add offer with 150% discount
       2. Capture error message
   
   - **6c. Not Found Error:**
     - Screenshot: Try to update non-existent offer → Error message
     - **Steps:**
       1. Try to update offer with ID 9999
       2. Capture "Offer not found" error
   
   - **6d. Foreign Key Constraint:**
     - Screenshot: Try to create offer with invalid food_item_id → Error message
     - **Steps:**
       1. Try to add offer with food_item_id = 9999
       2. Capture validation error

#### 7. **Database Verification**
   - **7a. Food Items in Database:**
     - Screenshot: Query `SELECT * FROM food_items;` in pgAdmin/psql
   
   - **7b. Offers in Database:**
     - Screenshot: Query `SELECT * FROM offers;` in pgAdmin/psql
   
   - **7c. Foreign Key Relationship:**
     - Screenshot: Join query showing food items with their offers
     - **SQL:**
       ```sql
       SELECT f.id, f.name, f.price, o.id as offer_id, o.discount_percentage
       FROM food_items f
       LEFT JOIN offers o ON f.id = o.food_item_id;
       ```

### Screenshot Tips:
- Use **Windows Snipping Tool** (Win+Shift+S) or **Lightshot** for screenshots
- Capture entire console window showing menu options and outputs
- Ensure text is readable (use zoom if needed)
- Label screenshots clearly (e.g., "Screenshot_01_Create_Meal.png")
- Include timestamps or sequence numbers in filenames
- For database screenshots, show query and results together

---

## G. Reflection Section

### What You Learned

1. **Object-Oriented Programming Principles:**
   - **Abstraction**: Used abstract `FoodItem` class to define common structure while allowing specific implementations in `Meal` and `Drink`
   - **Encapsulation**: Private fields with public getters/setters in `Offer` class, protected fields in `FoodItem`
   - **Inheritance**: `Meal` and `Drink` inherit from `FoodItem`, gaining shared attributes and behaviors
   - **Polymorphism**: Interface polymorphism with `PricedItem` and `Validatable`, method overriding for `getDescription()` and `calculatePrice()`

2. **Multi-Layer Architecture:**
   - Understanding separation of concerns: Model, Repository, Service, Controller layers
   - Each layer has distinct responsibilities, making code more maintainable and testable
   - Controllers handle UI, Services contain business logic, Repositories manage data access

3. **Database Integration with JDBC:**
   - Using JDBC drivers to connect Java applications to PostgreSQL
   - Writing SQL queries (SELECT, INSERT, UPDATE, DELETE)
   - Handling `ResultSet` to map database rows to Java objects
   - Using `PreparedStatement` for parameterized queries (preventing SQL injection)
   - Managing database connections and resources (try-with-resources)

4. **Database Relationships:**
   - Implementing foreign keys and referential integrity
   - Understanding `ON DELETE CASCADE` for maintaining data consistency
   - One-to-Many relationship between `food_items` and `offers`

5. **Exception Handling:**
   - Creating custom exceptions (`ValidationException`, `FoodItemNotValidException`)
   - Proper error handling and user-friendly error messages
   - Validating input before database operations

6. **Date Handling:**
   - Using `java.time.LocalDate` for date operations
   - Formatting dates for user input/output
   - Validating date ranges

### Challenges Faced

1. **Database Connection Setup:**
   - **Challenge**: Configuring JDBC driver and database credentials correctly
   - **Solution**: Created `DatabaseConnection` utility class to centralize connection management, used try-with-resources for automatic resource cleanup

2. **Mapping Database Results to Objects:**
   - **Challenge**: Converting `ResultSet` rows to Java objects, especially handling nullable fields
   - **Solution**: Created private helper methods like `mapResultSetToOffer()` in repositories to centralize mapping logic

3. **Foreign Key Relationships:**
   - **Challenge**: Ensuring data integrity when food items have offers - what happens when item is deleted?
   - **Solution**: Used `ON DELETE CASCADE` in foreign key constraint to automatically delete related offers

4. **Abstract Classes vs Interfaces:**
   - **Challenge**: Deciding when to use abstract class (`FoodItem`) vs interface (`PricedItem`, `Validatable`)
   - **Solution**: Used abstract class when sharing common implementation, interfaces for contracts that can be implemented by unrelated classes

5. **Date Validation:**
   - **Challenge**: Ensuring offer dates are valid (end date not before start date, dates not null)
   - **Solution**: Implemented comprehensive validation in `Offer.validate()` method

6. **Polymorphism Implementation:**
   - **Challenge**: Understanding when polymorphism occurs and how method resolution works
   - **Solution**: Practiced with examples - learned that method calls are resolved at runtime based on actual object type

### Benefits of JDBC and Multi-Layer Design

#### Benefits of JDBC:

1. **Database Agnostic (with limitations):**
   - JDBC provides a standard interface - can switch databases by changing driver
   - However, SQL syntax differences still need attention

2. **Direct SQL Control:**
   - Full control over SQL queries - can optimize for specific needs
   - Can use database-specific features through native SQL

3. **Performance:**
   - Direct connection to database - no ORM overhead
   - Can use connection pooling for better performance

4. **Learning Foundation:**
   - Understanding JDBC helps understand higher-level frameworks (Hibernate, JPA)
   - Important for debugging database-related issues

#### Benefits of Multi-Layer Design:

1. **Separation of Concerns:**
   - Each layer has a single responsibility
   - Controllers don't know about database, repositories don't know about business rules
   - Easy to understand and navigate codebase

2. **Maintainability:**
   - Changes in one layer don't affect others (loose coupling)
   - Example: Can change database without touching controllers
   - Example: Can add business logic in service without changing repository

3. **Testability:**
   - Can test each layer independently
   - Can mock repositories when testing services
   - Can test business logic without database connection

4. **Reusability:**
   - Services can be reused by different controllers (web, console, API)
   - Repositories can be reused by different services

5. **Scalability:**
   - Easy to add new features (e.g., new entity = new model, repository, service, controller)
   - Clear structure makes it easy for team members to work on different layers

6. **Debugging:**
   - When error occurs, can quickly identify which layer is responsible
   - Can add logging at specific layers

#### Example of Multi-Layer Benefits:

**Scenario**: Need to add email notification when offer is created

- **Without layers**: Would need to modify controller or repository (wrong responsibility)
- **With layers**: Simply add email logic in `OfferService.addOffer()` - business logic belongs in service layer

**Scenario**: Want to switch from console to web interface

- **Without layers**: Would need to rewrite everything
- **With layers**: Create new web controller, reuse existing services and repositories

---

## Conclusion

This project successfully demonstrates Object-Oriented Programming principles, JDBC database integration, and multi-layer architecture design. The separation of concerns through layers makes the application maintainable, testable, and scalable. The use of abstract classes, interfaces, and polymorphism creates a flexible design that can easily be extended with new food item types or features.

---

## Author Information

**Project**: Assignment 3 - OOP with JDBC  
**Course**: Object-Oriented Programming  
**Institution**: AITU (Astana IT University)  
**Date**: 2024
