## Endterm Project API — Spring Boot, Design Patterns & Component Principles

This project is the evolution of the Assignment 3 console app into a **Spring Boot RESTful API** with **JDBC**, **creational design patterns**, and **Component Principles**.

---

### A. Project Overview

- Domain:
  - `FoodItem` (abstract) with `Meal` and `Drink` subclasses.
  - `Offer` linked to `FoodItem` through `food_item_id`.
- Architecture:
  - `controller` — REST controllers (including cache clear endpoint).
  - `service` — business logic, validation, and cache integration for `getAllFoodItems()`.
  - `repository` — JDBC data access (PostgreSQL).
  - `model` — domain entities and interfaces.
  - `dto` — request/response models for REST.
  - `cache` — in-memory cache (Singleton) and cache keys.
  - `patterns` — Singleton, Factory, Builder implementations.
  - `utils`, `config`, `logging` — shared infrastructure.

---

### B. REST API Documentation

**Base URL**: `http://localhost:8080/api`

#### Food Items

- **GET** `/food-items` — list all food items.
- **GET** `/food-items/{id}` — get a single food item by id.
- **POST** `/food-items` — create a new food item.

  Request body:

  ```json
  {
    "name": "Burger Combo",
    "price": 9.99,
    "type": "MEAL"
  }
  ```

  Response body:

  ```json
  {
    "id": 1,
    "name": "Burger Combo",
    "price": 9.99,
    "description": "Meal: Burger Combo"
  }
  ```

- **PUT** `/food-items/{id}/price?price=12.50` — update price of an item.
- **DELETE** `/food-items/{id}` — delete an item by id.

#### Cache (manual clear)

- **DELETE** `/api/cache` — clear the entire in-memory cache (manual invalidation).
- **POST** `/api/cache/clear` — same as above (alternative for clients that prefer POST).

#### Offers

- **GET** `/offers` — list all offers.
- **GET** `/offers/active` — list currently active offers.
- **GET** `/offers/{id}` — get offer by id.
- **GET** `/offers/by-food/{foodItemId}` — offers for a given food item.

- **POST** `/offers` — create a new offer.

  Request body:

  ```json
  {
    "foodItemId": 1,
    "discountPercentage": 20,
    "description": "20% off combo",
    "startDate": "2025-01-01",
    "endDate": "2025-01-31"
  }
  ```

- **PUT** `/offers/{id}` — update an existing offer.
- **POST** `/offers/{id}/deactivate` — deactivate an offer.
- **DELETE** `/offers/{id}` — delete an offer.

#### Error responses

All errors go through `GlobalExceptionHandler` and return JSON:

```json
{
  "timestamp": "2025-01-01T12:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Food item ID must be positive"
}
```

You can test with **Postman** or **curl**:

```bash
curl http://localhost:8080/api/food-items
```

---

### C. Design Patterns

- **Singleton**
  - `config.DatabaseConfigManager` — single shared source of DB configuration, used by `utils.DatabaseConnection`.
  - `logging.LoggerService` — simple global logger used in controllers and exception handler.
  - `cache.InMemoryCacheManager` — single shared in-memory cache instance; see **Caching Layer** below.

- **Factory**
  - `patterns.factory.FoodItemFactory` — creates `Meal` or `Drink` based on `FoodItemRequest.type`. Returns base type `FoodItem` and is easily extendable (e.g. add `Dessert`).

- **Builder**
  - `patterns.builder.OfferBuilder` — builds complex `Offer` instances with fluent setters and optional fields. `OfferRestController` uses it both for creating new offers and updating existing ones (`fromExisting`).

---

### D. Component Principles (REP, CCP, CRP)

- **REP** — Reuse/Release Equivalence
  - Reusable modules grouped into components: `repository`, `service`, `patterns`, `utils`, `logging`, `config`.

- **CCP** — Common Closure Principle
  - Classes that change together are packaged together:
    - REST contracts in `controller` + `dto`.
    - Business rules in `service`.
    - JDBC code in `repository`.
    - Cross-cutting infra in `config` and `logging`.

- **CRP** — Common Reuse Principle
  - API clients only depend on `dto` and controller endpoints, not on repository/DB details.
  - Modules can reuse `patterns` or `utils` without pulling UI code.

---

### E. SOLID & OOP Summary

- **SRP**: Each package and class has a single responsibility (controllers = HTTP, services = rules, repos = DB).
- **OCP**: New `FoodItem` types or offer behaviors can be added with new subclasses/factory/ builder logic without modifying existing controllers.
- **LSP**: `Meal` and `Drink` are valid substitutes wherever `FoodItem` is used.
- **ISP**: Service and repository interfaces are focused on what their clients need.
- **DIP**: Controllers depend on service interfaces, services depend on repository interfaces, Spring injects implementations.

---

### F. Database Schema

- `food_items`:
  - `id` SERIAL PK
  - `name` VARCHAR NOT NULL
  - `price` DECIMAL NOT NULL
  - `type` VARCHAR NOT NULL (`Meal` or `Drink`)
- `offers`:
  - `id` SERIAL PK
  - `food_item_id` INT FK → `food_items(id)`
  - `discount_percentage` DECIMAL
  - `description` VARCHAR
  - `start_date`, `end_date` DATE
  - `is_active` BOOLEAN

The original SQL is in `resources/sheme.sql`; you can reuse it for the Spring Boot DB.

---

### G. System Architecture Diagram (Text)

- **Controller**
  - `FoodItemRestController`
  - `OfferRestController`
- **Service**
  - `FoodItemService` / `FoodItemServiceImpl`
  - `OfferService` / `OfferServiceImpl`
- **Repository**
  - `FoodItemRepository` / `FoodItemRepositoryImpl`
  - `OfferRepository` / `OfferRepositoryImpl`
- **Domain**
  - `FoodItem` (abstract) → `Meal`, `Drink`
  - `Offer`
- **Patterns / Infra**
  - `config.DatabaseConfigManager` (Singleton)
  - `logging.LoggerService` (Singleton)
  - `cache.InMemoryCacheManager` (Singleton), `cache.SimpleCache`, `cache.CacheKeys`
  - `patterns.factory.FoodItemFactory`
  - `patterns.builder.OfferBuilder`
  - `utils.DatabaseConnection`
- **Cache**
  - `controller.CacheController` — manual cache clear via `DELETE /api/cache` or `POST /api/cache/clear`.

For your UML diagram, you can export a class diagram as `docs/uml.png`.

---

### J. Caching Layer (In-Memory Cache)

A simple in-memory cache is used to avoid repeated database queries for frequently requested data.

#### What was implemented

1. **Singleton cache instance**
   - `cache.InMemoryCacheManager` is a **Singleton**: one shared instance across the application (`getInstance()` with double-checked locking).
   - Storage is a single in-memory `ConcurrentHashMap` (thread-safe for concurrent REST requests).

2. **Cached method**
   - **`getAllFoodItems()`** is cached. The first call loads the list from the database and stores it under the key `CacheKeys.FOOD_ITEMS_ALL`. Subsequent calls return the cached list without hitting the database.

3. **Automatic invalidation**
   - The cache for food items is cleared whenever data changes so that clients never get stale data:
     - After **add** (addFoodItem): cache is invalidated.
     - After **update** (updatePrice): cache is invalidated.
     - After **delete** (deleteFoodItem or deleteFoodItemById): cache is invalidated.
   - Invalidation is done by removing the key `CacheKeys.FOOD_ITEMS_ALL` in `FoodItemServiceImpl` (private method `invalidateFoodItemsCache()`).

4. **Manual clear**
   - **DELETE** `/api/cache` or **POST** `/api/cache/clear` clears the entire cache. Use this after bulk updates or when you want to force fresh data from the database.

#### Design (SOLID and layers)

- **Interface** `cache.SimpleCache` defines `get`, `put`, `remove`, `clear`. The service layer depends on the cache abstraction (DIP). `InMemoryCacheManager` implements this interface.
- **Single responsibility**: the cache only stores and retrieves data; the service decides *what* to cache and *when* to invalidate.
- **Layered architecture**: the cache is used only inside the **service** layer (`FoodItemServiceImpl`). Controllers do not access the cache for business data; they only expose a cache-clear endpoint that delegates to the Singleton. The repository layer is unchanged and has no cache awareness.
- **Cache keys** are centralized in `cache.CacheKeys` to avoid magic strings and keep key management in one place.

---

### H. How to Run the Spring Boot Application

1. **Prerequisites**
   - Java 17+
   - Maven
   - PostgreSQL database `food_delivery`

2. **Configure DB**
   - Update `src/main/resources/application.properties`:
     - `spring.datasource.url`
     - `spring.datasource.username`
     - `spring.datasource.password`
   - Run `resources/sheme.sql` to create tables and seed data.

3. **Build & Run**

   ```bash
   mvn clean package
   mvn spring-boot:run
   ```

4. **Test**
   - Use Postman / curl against `http://localhost:8080/api/...`.

---

### I. Reflection

- Migrating from console + JDBC to Spring Boot required separating external API (controllers + DTOs) from the internal domain and infrastructure.
- Implementing Singleton, Factory, and Builder patterns inside the REST architecture shows how classical patterns still matter even when using a framework.
- Component principles helped structure packages so that changes in one area (REST, DB, patterns) have minimal impact on others.
- The final system demonstrates full integration of **JDBC**, **SOLID**, **design patterns**, **component principles**, and a **RESTful API**. An **in-memory caching layer** (Singleton) caches `getAllFoodItems()` and invalidates on updates/deletes, with a manual clear endpoint, without breaking the layered architecture.

