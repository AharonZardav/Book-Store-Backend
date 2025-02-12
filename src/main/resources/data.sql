-- Create users table
CREATE TABLE users (
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    phone VARCHAR(20) NOT NULL,
    address VARCHAR(255) NOT NULL,
    username VARCHAR(255),
    password VARCHAR(255) NOT NULL,
    role VARCHAR(255) DEFAULT 'USER',
    PRIMARY KEY (username)
);

--Create items table
CREATE TABLE items (
    item_id INT AUTO_INCREMENT,
    title VARCHAR(255) UNIQUE NOT NULL,
    image_path VARCHAR(255) NOT NULL,
    price INT NOT NULL,
    quantity INT NOT NULL,
    PRIMARY KEY (item_id)
);

--Create orders table
CREATE TABLE orders (
    order_id INT AUTO_INCREMENT,
    username VARCHAR(255) NOT NULL,
    order_date DATE NOT NULL,
    shipping_address VARCHAR(255) NOT NULL,
    order_status VARCHAR(10) DEFAULT 'OPEN',
    PRIMARY KEY (order_id),
    FOREIGN KEY (username) REFERENCES users(username)
);

--Create linking table for orders and items
CREATE TABLE orders_items (
    order_id INT NOT NULL,
    item_id INT NOT NULL,
    quantity_in_order INT NOT NULL,
    PRIMARY KEY (order_id, item_id),
    FOREIGN KEY (order_id) REFERENCES orders(order_id),
    FOREIGN KEY (item_id) REFERENCES items(item_id)
);