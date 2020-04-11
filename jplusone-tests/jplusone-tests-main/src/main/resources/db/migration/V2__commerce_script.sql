/*
 * Copyright (c) 2020 Adam Gaj
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

CREATE TABLE client_profile (
    id INT PRIMARY KEY,
    photo_link VARCHAR(50) NOT NULL
);

CREATE TABLE client (
    id INT PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    client_profile_id INT,
    FOREIGN KEY (client_profile_id) REFERENCES client_profile(id)
);

CREATE TABLE user (
    id INT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    client_id INT,
    FOREIGN KEY (client_id) REFERENCES client(id)
);

CREATE TABLE orders (
    id INT PRIMARY KEY,
    note VARCHAR(50) NOT NULL,
    client_id INT NOT NULL,
    FOREIGN KEY (client_id) REFERENCES client(id)
);

CREATE TABLE manufacturer (
    id INT PRIMARY KEY,
    name VARCHAR(50) NOT NULL
);

CREATE TABLE product (
    id INT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    manufacturer_id INT NOT NULL,
    FOREIGN KEY (manufacturer_id) REFERENCES manufacturer(id)
);

CREATE TABLE order_product (
    order_id INT NOT NULL,
    product_id INT NOT NULL,
    PRIMARY KEY (order_id, product_id),
    FOREIGN KEY (order_id) REFERENCES orders(id),
    FOREIGN KEY (product_id) REFERENCES product(id)
);

INSERT INTO client_profile(id, photo_link) VALUES (1, 'http://example.com/me.jpg');

INSERT INTO client(id, first_name, last_name, client_profile_id) VALUES (1, 'John', 'Smith', 1);
INSERT INTO client(id, first_name, last_name, client_profile_id) VALUES (2, 'James', 'Jones', null);

INSERT INTO user(id, username, client_id) VALUES (1, 'john.smith', 1);
INSERT INTO user(id, username, client_id) VALUES (2, 'james.jones', 2);

INSERT INTO manufacturer(id, name) VALUES (1, 'Wayne Enterpreises');

INSERT INTO product(id, name, manufacturer_id) VALUES (1, 'Batmobil', 1);

INSERT INTO orders(id, note, client_id) VALUES (1, 'Hurry Up!', 1);

INSERT INTO order_product(order_id, product_id) VALUES (1, 1);






