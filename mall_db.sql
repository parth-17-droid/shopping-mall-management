-- Create the database
CREATE DATABASE IF NOT EXISTS mall_db;
USE mall_db;

-- Create shops table
CREATE TABLE IF NOT EXISTS shops (
    id INT AUTO_INCREMENT PRIMARY KEY,
    shop_name VARCHAR(100) NOT NULL,
    shop_category VARCHAR(100) NOT NULL
);

-- Create customers table
CREATE TABLE IF NOT EXISTS customers (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    contact VARCHAR(20) NOT NULL
);

-- Create purchases table
CREATE TABLE IF NOT EXISTS purchases (
    id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT NOT NULL,
    shop_id INT NOT NULL,
    item VARCHAR(100) NOT NULL,
    amount DOUBLE NOT NULL,
    purchase_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES customers(id) ON DELETE CASCADE,
    FOREIGN KEY (shop_id) REFERENCES shops(id) ON DELETE CASCADE
);

-- Create indexes for better performance
CREATE INDEX idx_customer_name ON customers(name);
CREATE INDEX idx_shop_name ON shops(shop_name);
CREATE INDEX idx_purchase_date ON purchases(purchase_date);
