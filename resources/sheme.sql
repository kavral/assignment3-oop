--Dropping all tables
DROP TABLE IF EXISTS food_items;
DROP TABLE IF EXISTS offers;

-- Creating a food_items table
CREATE TABLE food_items (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255),
    price DECIMAL(10, 2),
    type VARCHAR(50)
);

-- Creating an offers table with foreign key to food_items
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