DROP TABLE IF EXISTS offers;
DROP TABLE IF EXISTS food_items;

CREATE TABLE food_items (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    price DECIMAL(10, 2) NOT NULL CHECK (price > 0),
    type VARCHAR(50) NOT NULL CHECK (type IN ('Meal', 'Drink'))
);

CREATE TABLE offers (
    id SERIAL PRIMARY KEY,
    discount_percentage DECIMAL(5, 2) NOT NULL CHECK (discount_percentage > 0 AND discount_percentage <= 100),
    description VARCHAR(500),
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    CONSTRAINT fk_offers_food_item FOREIGN KEY (id) REFERENCES food_items(id) ON DELETE CASCADE,
    CONSTRAINT chk_offer_dates CHECK (end_date >= start_date)
);

INSERT INTO food_items (name, price, type) VALUES
    ('Chicken Wrap', 6.75, 'Meal'),
    ('Beef Steak', 18.90, 'Meal'),
    ('Spaghetti Bolognese', 11.40, 'Meal'),
    ('Fish and Chips', 10.20, 'Meal'),
    ('Grilled Chicken', 13.50, 'Meal'),
    ('Veggie Burger', 7.80, 'Meal'),
    ('Tacos', 9.60, 'Meal'),
    ('Milkshake', 4.50, 'Drink'),
    ('Lemonade', 2.80, 'Drink'),
    ('Green Tea', 2.60, 'Drink'),
    ('Latte', 3.70, 'Drink'),
    ('Espresso', 2.40, 'Drink'),
    ('Smoothie', 4.90, 'Drink'),
    ('Iced Coffee', 3.30, 'Drink');

INSERT INTO offers (discount_percentage, description, start_date, end_date, is_active) VALUES
    (12.00, 'Healthy salad promotion', '2025-02-01', '2025-04-30', TRUE),
    (8.00, 'Fresh juice discount', '2025-03-01', '2025-05-31', TRUE),
    (10.00, 'Morning coffee deal', '2025-01-01', '2025-12-31', TRUE),
    (14.00, 'Wrap Wednesday', '2025-02-10', '2025-08-10', TRUE),
    (20.00, 'Steak lovers promo', '2025-03-01', '2025-03-31', FALSE),
    (11.00, 'Italian pasta week', '2025-04-01', '2025-04-14', TRUE),
    (9.00, 'Fish Friday discount', '2025-01-01', '2025-12-31', TRUE),
    (13.50, 'Protein meal deal', '2025-05-01', '2025-07-31', TRUE),
    (10.00, 'Vegetarian special', '2025-02-01', '2025-06-01', TRUE),
    (7.00, 'Taco Tuesday', '2025-01-01', '2025-12-31', TRUE);
