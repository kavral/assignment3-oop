-- Create food_items table
CREATE TABLE IF NOT EXISTS food_items (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    type VARCHAR(50) NOT NULL
);

-- Create offers table with foreign key to food_items
CREATE TABLE IF NOT EXISTS offers (
    id SERIAL PRIMARY KEY,
    food_item_id INTEGER NOT NULL,
    discount_percentage DECIMAL(5, 2) NOT NULL,
    description VARCHAR(500),
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (food_item_id) REFERENCES food_items(id) ON DELETE CASCADE
);

-- Create index on food_item_id for better query performance
CREATE INDEX IF NOT EXISTS idx_offers_food_item_id ON offers(food_item_id);
CREATE INDEX IF NOT EXISTS idx_offers_active ON offers(is_active);
