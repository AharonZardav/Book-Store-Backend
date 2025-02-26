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
    price DECIMAL(10,2) NOT NULL,
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
    FOREIGN KEY (username) REFERENCES users(username) ON DELETE CASCADE
);

--Create linking table for orders and items
CREATE TABLE orders_items (
    order_id INT NOT NULL,
    item_id INT NOT NULL,
    quantity_in_order INT NOT NULL,
    PRIMARY KEY (order_id, item_id),
    FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE,
    FOREIGN KEY (item_id) REFERENCES items(item_id) ON DELETE CASCADE
);

--Create favorites item list
CREATE TABLE favorites_list (
    username VARCHAR(255) NOT NULL,
    item_id INT NOT NULL,
    PRIMARY KEY (username, item_id),
    FOREIGN KEY (username) REFERENCES users(username) ON DELETE CASCADE,
    FOREIGN KEY (item_id) REFERENCES items(item_id) ON DELETE CASCADE
);

--username: aharonzardav || password: Az123321@
INSERT INTO users(first_name, last_name, email, phone, address, username, password)
VALUES('Aharon', 'Zardav', 'aharonzazrdav8@gmail.com', '0503337936', 'Ahisamach, Shivaat Hminim 21', 'aharonzardav', '$2a$10$sBSKLbWjWLwhqor4yNKz8e/pbHnDi2TnfPnJDbK8yGQwqZ5ZRuJEm');

INSERT INTO items(title, image_path, price, quantity)
VALUES
    ('השקעות לעצלנים','https://www.e-vrit.co.il/Images/Products/NewBO/Products/21677/tamir_mandovsky500x790.jpg', 49.50, 20),
    ('העץ הנדיב','https://www.e-vrit.co.il/Images/Products/ModanMasters/haezhanadiv_master.jpg', 34.90, 20),
    ('הארי פוטר ואבן החכמים','https://upload.wikimedia.org/wikipedia/he/3/3c/%D7%94%D7%90%D7%A8%D7%99_%D7%A4%D7%95%D7%98%D7%A8_%D7%95%D7%90%D7%91%D7%9F_%D7%94%D7%97%D7%9B%D7%9E%D7%99%D7%9D_%D7%A1%D7%A4%D7%A8.jpeg', 72.79, 20),
    ('יומנו של חנון','https://upload.wikimedia.org/wikipedia/he/f/fc/Wimpy.jpg', 39.90, 20),
    ('מוקף באידיוטים','https://images-evrit.yit.co.il/Images/Products/covers_2019_b/mukaf_master.jpg', 49.90, 20),
    ('ארבעת ההסכמות','https://media.getmood.io/warehouse/dynamic/197580.jpg', 44.40, 20),
    ('cant hurt me','https://m.media-amazon.com/images/I/81gTRv2HXrL.jpg', 49.90, 20),
    ('הרגלים אטומיים','https://www.elderech.co.il/wp-content/uploads/2022/03/%D7%A2%D7%99%D7%A6%D7%95%D7%91-%D7%9C%D7%9C%D7%90-%D7%A9%D7%9D-2022-03-07T172841.350-e1646751312452.png', 49.90, 20),
    ('אבא עשיר אבא עני','https://images-evrit.yit.co.il/Images/Products/NewBO/Products/24654/richDad_Master.jpg', 49.90, 20),
    ('הפסיכולוגיה של הכסף','https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQGFCVPD77PhVH4ld7tUbDuXKOsfxwJGFm7bg&s', 49.90, 20);