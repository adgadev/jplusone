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

CREATE TABLE genre (
    id INT PRIMARY KEY,
    name VARCHAR(50) NOT NULL
);

CREATE TABLE author (
    id INT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    genre_id INT NOT NULL,
    FOREIGN KEY (genre_id) REFERENCES genre(id)
);

CREATE TABLE book (
    id INT PRIMARY KEY,
    title VARCHAR(50) NOT NULL,
    author_id INT NOT NULL,
    FOREIGN KEY (author_id) REFERENCES author(id)
);

INSERT INTO genre(id, name) VALUES (1, 'Thriller');

INSERT INTO author(id, name, genre_id) VALUES (1, 'Mario Puzo', 1);
INSERT INTO author(id, name, genre_id) VALUES (2, 'Tom Rob Smith', 1);

INSERT INTO book(id, title, author_id) VALUES (1, 'Godfather', 1);
INSERT INTO book(id, title, author_id) VALUES (2, 'Child 44', 2);
