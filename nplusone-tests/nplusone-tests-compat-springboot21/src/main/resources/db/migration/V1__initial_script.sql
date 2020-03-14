CREATE TABLE book (
    id INT PRIMARY KEY,
    title VARCHAR(50) NOT NULL,
    author_id INT
);

CREATE TABLE author (
    id INT PRIMARY KEY,
    name VARCHAR(50) NOT NULL
);

INSERT INTO author(id, name) VALUES (1, 'Mario Puzo');
INSERT INTO author(id, name) VALUES (2, 'Tom Rob Smith');

INSERT INTO book(id, title, author_id) VALUES (1, 'Godfather', 1);
INSERT INTO book(id, title, author_id) VALUES (2, 'Child 44', 2);
