
-- CATEGORY

INSERT INTO category (name, description)
VALUES
('Fiction', 'Imaginative and narrative literature'),
('Science', 'Books related to natural and applied sciences'),
('History', 'Historical events, biographies, and chronicles'),
('Technology', 'Books about programming, IT, and innovation');

-- PUBLISHER

INSERT INTO publisher (name, address)
VALUES
('Penguin Random House', '1745 Broadway, New York, NY, USA'),
('HarperCollins', '195 Broadway, New York, NY, USA'),
('Oxford University Press', 'Great Clarendon Street, Oxford, UK');


-- AUTHOR

INSERT INTO author (name, bio)
VALUES
('J.K. Rowling', 'British author, best known for the Harry Potter series'),
('George Orwell', 'English novelist, essayist, and critic'),
('Stephen King', 'American author of horror, supernatural fiction, suspense'),
('Robert C. Martin', 'Software engineer and author of Clean Code'),
('Yuval Noah Harari', 'Historian and author of Sapiens: A Brief History of Humankind');


-- BOOK

INSERT INTO book (title, isbn, edition, pages, description, publication_year, language, category_id, publisher_id)
VALUES
('Harry Potter and the Sorcerer''s Stone', '9780747532699', '1st', 223, 'The first book in the Harry Potter series.', 1997, 'English', 1, 1),
('1984', '9780451524935', '1st', 328, 'Dystopian social science fiction novel.', 1949, 'English', 3, 2),
('Clean Code', '9780132350884', '1st', 464, 'A Handbook of Agile Software Craftsmanship.', 2008, 'English', 4, 2),
('Sapiens: A Brief History of Humankind', '9780062316097', '1st', 443, 'Explores the history of humankind.', 2011, 'English', 3, 3),
('The Shining', '9780385121675', '1st', 447, 'Horror novel by Stephen King.', 1977, 'English', 1, 2);


-- BOOK_AUTHOR

-- Harry Potter → J.K. Rowling
INSERT INTO book_author (book_id, author_id) VALUES (1, 1);

-- 1984 → George Orwell
INSERT INTO book_author (book_id, author_id) VALUES (2, 2);

-- Clean Code → Robert C. Martin
INSERT INTO book_author (book_id, author_id) VALUES (3, 4);

-- Sapiens → Yuval Noah Harari
INSERT INTO book_author (book_id, author_id) VALUES (4, 5);

-- The Shining → Stephen King
INSERT INTO book_author (book_id, author_id) VALUES (5, 3);