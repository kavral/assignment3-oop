# Assignment 3 OOP — Food Delivery Platform

Console application for managing food items (Meals, Drinks) and offers. Built with SOLID architecture, JDBC (PostgreSQL), and advanced OOP features.

---

## A. SOLID Documentation

### SRP (Single Responsibility Principle) — one reason to change per class

| Class / Layer | Responsibility |
|---------------|----------------|
| **FoodItemController** | Handle user input and output for food items only. |
| **OfferController** | Handle user input and output for offers only. |
| **FoodItemServiceImpl** | Business rules and validation for food items. |
| **OfferServiceImpl** | Business rules and validation for offers. |
| **FoodItemRepositoryImpl** | JDBC data access for `food_items` table only. |
| **OfferRepositoryImpl** | JDBC data access for `offers` table only. |
| **FoodItem** (abstract) | Represent a generic food item; subclasses define behavior. |
| **ReflectionUtil** | Inspect object structure via reflection only. |

### OCP (Open–Closed Principle) — open for extension, closed for modification

- **FoodItem** is an abstract base class. New kinds of items (e.g. `Dessert`) are added by creating a new subclass and implementing `calculatePrice()` and `getDescription()` **without changing** existing `Meal`/`Drink` or repository logic.
- New repository or service behavior can be added via new interfaces/implementations without changing existing controllers.

### LSP (Liskov Substitution Principle) — subclasses substitutable for base type

- **Meal** and **Drink** extend **FoodItem**. Any code that works with `FoodItem` (e.g. `List<FoodItem>`, service/repository) works correctly when given a `Meal` or `Drink`.
- They override only abstract methods and do not strengthen preconditions or weaken postconditions.

### ISP (Interface Segregation Principle) — narrow interfaces

- **Validatable** — only `validate()` (and optional default/static helpers).
- **PricedItem** — extends `Validatable` and adds `getPrice()` (and optional `getFormattedPrice()`).
- **FoodItemService** / **OfferService** — only the operations needed by controllers.
- **CrudRepository\<T\>** — generic CRUD; **FoodItemRepository** adds food-specific methods (e.g. `findAllSortedByName`, `updatePrice`, `deleteByName`).

### DIP (Dependency Inversion Principle) — depend on abstractions

- **Controllers** depend on **service interfaces** (`FoodItemService`, `OfferService`), not concrete classes. Implementations are injected via constructor in `Main`.
- **Services** depend on **repository interfaces** (`FoodItemRepository`, `OfferRepository`), not JDBC implementations. Implementations are injected via constructor.
- **Main** is the composition root: it creates concrete repositories and services and passes them into controllers.

---

## B. Advanced OOP Features

### Generics

- **CrudRepository\<T\>**: `save(T entity)`, `findById(int id)`, `findAll()`, `deleteById(int id)`. Type-safe reuse for any entity (e.g. `FoodItemRepository extends CrudRepository<FoodItem>`).
- **List\<FoodItem\>**, **List\<Offer\>** used in service/controller return types.

### Lambdas

- **Sorting**: In `FoodItemRepositoryImpl.findAllSortedByName()` — `items.sort(Comparator.comparing(FoodItem::getName, String.CASE_INSENSITIVE_ORDER));` (method reference + comparator).
- **Iteration**: In controllers — `items.forEach(item -> System.out.println(...))`.

### Reflection

- **ReflectionUtil** (`utils/ReflectionUtil.java`): `describe(Object obj)` uses `Class.getDeclaredFields()`, `getMethods()`, `Field.get(obj)` to print class name, fields (with values), and public methods. Used in **Food Items → option 8 (Reflection Demo)**.

### Interface default / static methods

- **Validatable**: `default String validationMessage()` and `static boolean isValid(Validatable v)`.
- **PricedItem**: `default String getFormattedPrice()`.

---

## C. OOP Documentation

### Abstract class and subclasses

- **FoodItem** (abstract): fields `id`, `name`, `price` (private; getters/setters). Abstract: `calculatePrice()`, `getDescription()`. Concrete: `getPrice()`, `validate()`, `basicInfo()`.
- **Meal** extends **FoodItem**: adds optional `calories`; overrides `calculatePrice()`, `getDescription()`.
- **Drink** extends **FoodItem**: adds optional `volumeMl`; overrides `calculatePrice()`, `getDescription()`.

### Composition / aggregation

- **Offer** has a **food_item_id** (FK to `food_items`). Conceptually: Offer aggregates a reference to a FoodItem (composition/aggregation). No inheritance between Offer and FoodItem.

### Polymorphism

- Repository returns `List<FoodItem>`; list can contain `Meal` and `Drink`. Code uses `item.getDescription()` and `item.calculatePrice()` — behavior depends on runtime type.
- Controllers and services work with `FoodItem`; they don’t need to know concrete type for display or persistence (type stored in DB as `Meal`/`Drink`).

### UML (high-level)

```
                    <<interface>>
                    Validatable
                    + validate(): boolean
                           ^
                    PricedItem (extends Validatable)
                    + getPrice(): double
                           ^
                    FoodItem (abstract, implements PricedItem)
                    - id, name, price
                    + calculatePrice(): double (abstract)
                    + getDescription(): String (abstract)
                    + basicInfo(): String
                    + validate(): boolean
                    / getPrice(), getters/setters
                    ^           ^
              Meal              Drink
              - calories         - volumeMl

    Controller --> Service (interface)
    Service --> Repository (interface)
    Offer --> food_item_id (FK to FoodItem)
```

---

## D. Database Section

### Schema

- **food_items**: `id` (SERIAL PK), `name` (VARCHAR NOT NULL), `price` (DECIMAL NOT NULL, CHECK &gt; 0), `type` (VARCHAR NOT NULL, CHECK IN ('Meal','Drink')).
- **offers**: `id` (SERIAL PK), `food_item_id` (INT NOT NULL, FK → food_items.id ON DELETE CASCADE), `discount_percentage` (DECIMAL, CHECK 0–100), `description`, `start_date`, `end_date` (NOT NULL), `is_active` (DEFAULT TRUE), CHECK `end_date >= start_date`.

### Constraints

- PKs on `food_items.id` and `offers.id`.
- FK `offers.food_item_id` → `food_items(id)` ON DELETE CASCADE.
- CHECKs: `price > 0`, `type IN ('Meal','Drink')`, `discount_percentage` in (0, 100], `end_date >= start_date`.

### Sample inserts

See `resources/sheme.sql`: sample rows for `food_items` (e.g. Burger, Pizza, Cola) and `offers` (e.g. 10% off Burger, 15% off Pizza).

---

## E. Architecture Explanation

- **Controller**: Reads user input (Scanner), calls service methods, prints results. Catches runtime exceptions and shows messages. No SQL or direct repository access.
- **Service**: Validates input and business rules, calls repository. Throws `ValidationException` / `FoodItemNotValidException` on invalid data or missing entities.
- **Repository**: Executes JDBC (using `DatabaseConnection`), maps `ResultSet` to entities. No business rules.

Example flow: **Add Meal**  
User enters name and price → Controller creates `Meal(0, name, price)` → Controller calls `foodItemService.addFoodItem(meal)` → Service validates (e.g. `Validatable.isValid(item)`), calls `repository.save(item)` → Repository INSERTs and sets ID on entity, returns it → Service returns entity → Controller prints "Meal added with ID: …".

---

## F. Execution Instructions

### Requirements

- **Java**: 17+ (or 11+; project uses standard JDBC and `java.time`).
- **PostgreSQL**: Server running; database `food_delivery` created.
- **Driver**: PostgreSQL JDBC driver on classpath (e.g. in IDE libraries or Maven/Gradle).

### Database setup

1. Create database: `CREATE DATABASE food_delivery;`
2. Run `resources/sheme.sql` in your SQL client (creates tables, constraints, sample data).
3. In `utils/DatabaseConnection.java`, set `URL`, `USER`, `PASSWORD` to your environment (or use config/env later).

### Compile and run

- **IDE**: Open project, set JDBC driver, run `Main.main()`.
- **Command line**:  
  `javac -cp ".;path/to/postgresql.jar" src/**/*.java` (adjust path and package roots).  
  `java -cp ".;path/to/postgresql.jar" Main` (from `src` or with correct package path).

---

## G. Screenshots

---

## H. Reflection (Learning & Challenges)

- **What you learned**: Separating UI, business logic, and data access makes the code easier to test and change. Depending on interfaces (DIP) allows swapping implementations. Abstract base class + subclasses (OCP/LSP) keep the model clear and extensible.
- **Challenges**: Wiring dependencies manually in `Main`; ensuring repository returns entities with IDs set; handling validation consistently across controller and service.
- **Value of SOLID**: Changes in one layer (e.g. new validation rule in service or new sort in repository) don’t force changes in others. New food types or new repositories can be added with minimal edits to existing code.
