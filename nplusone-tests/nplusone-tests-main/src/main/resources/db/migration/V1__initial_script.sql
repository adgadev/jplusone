CREATE TABLE table_a (
    id INT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    domain_b_id INT
);

CREATE TABLE table_b (
    id INT PRIMARY KEY,
    name VARCHAR(50) NOT NULL
);

INSERT INTO table_b(id, name) VALUES (1, 'first-object-b');
INSERT INTO table_b(id, name) VALUES (2, 'second-object-b');

INSERT INTO table_a(id, name, domain_b_id) VALUES (1, 'first-object-a', 1);
INSERT INTO table_a(id, name, domain_b_id) VALUES (2, 'second-object-a', 2);
