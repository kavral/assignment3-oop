-- =============================================
-- Food Delivery DB Schema (Assignment 3 OOP)
-- =============================================

-- Drop tables (order matters due to FK)
DROP TABLE IF EXISTS offers;
DROP TABLE IF EXISTS food_items;

-- ---------------------------------------------
-- food_items: base entity storage (Meal/Drink)
-- ---------------------------------------------
CREATE TABLE food_items (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    price DECIMAL(10, 2) NOT NULL CHECK (price > 0),
    type VARCHAR(50) NOT NULL CHECK (type IN ('Meal', 'Drink'))
);

-- ---------------------------------------------
-- offers: aggregation to food_items (FK)
-- ---------------------------------------------
CREATE TABLE offers (
    id SERIAL PRIMARY KEY,
    food_item_id INT NOT NULL,
    discount_percentage DECIMAL(5, 2) NOT NULL CHECK (discount_percentage > 0 AND discount_percentage <= 100),
    description VARCHAR(500),
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    CONSTRAINT fk_offers_food_item FOREIGN KEY (food_item_id) REFERENCES food_items(id) ON DELETE CASCADE,
    CONSTRAINT chk_offer_dates CHECK (end_date >= start_date)
);

-- ---------------------------------------------
-- Sample INSERT statements
-- ---------------------------------------------
INSERT INTO food_items (name, price, type) VALUES
    ('Burger', 8.99, 'Meal'),
    ('Pizza Margherita', 12.50, 'Meal'),
    ('Caesar Salad', 7.25, 'Meal'),
    ('Cola', 2.50, 'Drink'),
    ('Orange Juice', 3.00, 'Drink'),
    ('Coffee', 2.99, 'Drink');

INSERT INTO offers (food_item_id, discount_percentage, description, start_date, end_date, is_active) VALUES
    (1, 10.00, 'Lunch special on Burgers', '2025-01-01', '2025-12-31', TRUE),
    (2, 15.00, 'Weekend pizza deal', '2025-01-15', '2025-02-28', TRUE),
    (4, 5.00, 'Combo drink discount', '2025-01-01', '2025-06-30', TRUE);
