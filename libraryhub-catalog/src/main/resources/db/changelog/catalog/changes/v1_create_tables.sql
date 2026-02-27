
CREATE TABLE category (
    category_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(150) NOT NULL UNIQUE,
    description TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE
);


CREATE TABLE publisher (
    publisher_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(200) NOT NULL UNIQUE,
    address TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE
);


CREATE TABLE author (
    author_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    bio TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE INDEX idx_author_name ON author(name);

CREATE TABLE book (
    book_id BIGSERIAL PRIMARY KEY,
    title VARCHAR(300) NOT NULL,
    isbn VARCHAR(20) NOT NULL UNIQUE,
    edition VARCHAR(50),
    pages INT CHECK (pages > 0),
    description TEXT,
    publication_year INT CHECK (publication_year > 1000),
    language VARCHAR(100),
    category_id BIGINT NOT NULL,
    publisher_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,

    CONSTRAINT fk_book_category
        FOREIGN KEY (category_id)
        REFERENCES category(category_id)
        ON DELETE RESTRICT,

    CONSTRAINT fk_book_publisher
        FOREIGN KEY (publisher_id)
        REFERENCES publisher(publisher_id)
        ON DELETE RESTRICT
);

CREATE INDEX idx_book_title ON book(title);
CREATE INDEX idx_book_category ON book(category_id);
CREATE INDEX idx_book_publisher ON book(publisher_id);


CREATE TABLE book_author (
    book_id BIGINT NOT NULL,
    author_id BIGINT NOT NULL,

    PRIMARY KEY (book_id, author_id),

    CONSTRAINT fk_bookauthor_book
        FOREIGN KEY (book_id)
        REFERENCES book(book_id)
        ON DELETE CASCADE,

    CONSTRAINT fk_bookauthor_author
        FOREIGN KEY (author_id)
        REFERENCES author(author_id)
        ON DELETE CASCADE
);