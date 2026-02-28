package com.libraryhub.catalog.service.impl;

import com.libraryhub.catalog.dto.BookRequest;
import com.libraryhub.catalog.dto.BookResponse;
import com.libraryhub.catalog.entity.Author;
import com.libraryhub.catalog.entity.Book;
import com.libraryhub.catalog.entity.Category;
import com.libraryhub.catalog.entity.Publisher;
import com.libraryhub.catalog.repository.AuthorRepository;
import com.libraryhub.catalog.repository.BookRepository;
import com.libraryhub.catalog.repository.CategoryRepository;
import com.libraryhub.catalog.repository.PublisherRepository;
import com.libraryhub.catalog.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;
    private final PublisherRepository publisherRepository;
    private final AuthorRepository authorRepository;

    @Override
    public BookResponse create(BookRequest request) {

        bookRepository.findByIsbnAndIsDeletedFalse(request.getIsbn())
                .ifPresent(b -> {
                    throw new RuntimeException("ISBN already exists");
                });

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Publisher publisher = publisherRepository.findById(request.getPublisherId())
                .orElseThrow(() -> new RuntimeException("Publisher not found"));

        Set<Author> authors = authorRepository.findAllById(request.getAuthorIds())
                .stream()
                .collect(Collectors.toSet());

        Book book = new Book();
        book.setTitle(request.getTitle());
        book.setIsbn(request.getIsbn());
        book.setEdition(request.getEdition());
        book.setPages(request.getPages());
        book.setDescription(request.getDescription());
        book.setPublicationYear(request.getPublicationYear());
        book.setLanguage(request.getLanguage());
        book.setCategory(category);
        book.setPublisher(publisher);
        book.setAuthors(authors);
        book.setCreatedAt(LocalDateTime.now());
        book.setUpdatedAt(LocalDateTime.now());
        book.setDeleted(false);

        book = bookRepository.save(book);

        return mapToResponse(book);
    }

    @Override
    public BookResponse update(Long id, BookRequest request) {

        Book book = bookRepository.findById(id)
                .filter(b -> !b.isDeleted())
                .orElseThrow(() -> new RuntimeException("Book not found"));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Publisher publisher = publisherRepository.findById(request.getPublisherId())
                .orElseThrow(() -> new RuntimeException("Publisher not found"));

        Set<Author> authors = authorRepository.findAllById(request.getAuthorIds())
                .stream()
                .collect(Collectors.toSet());

        book.setTitle(request.getTitle());
        book.setEdition(request.getEdition());
        book.setPages(request.getPages());
        book.setDescription(request.getDescription());
        book.setPublicationYear(request.getPublicationYear());
        book.setLanguage(request.getLanguage());
        book.setCategory(category);
        book.setPublisher(publisher);
        book.setAuthors(authors);
        book.setUpdatedAt(LocalDateTime.now());

        return mapToResponse(bookRepository.save(book));
    }

    @Override
    @Transactional(readOnly = true)
    public BookResponse getById(Long id) {
        Book book = bookRepository.findById(id)
                .filter(b -> !b.isDeleted())
                .orElseThrow(() -> new RuntimeException("Book not found"));

        return mapToResponse(book);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookResponse> getAll() {
        return bookRepository.findAllByIsDeletedFalse()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public void delete(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        book.setDeleted(true);
        book.setUpdatedAt(LocalDateTime.now());
        bookRepository.save(book);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BookResponse> search(String keyword,
                                     int page,
                                     int size,
                                     String sortBy,
                                     String direction) {

        Sort sort = direction.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() :
                Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Book> bookPage = bookRepository.searchBooks(keyword, pageable);

        return bookPage.map(this::mapToResponse);
    }
    private BookResponse mapToResponse(Book book) {

        Set<String> authorNames = book.getAuthors()
                .stream()
                .map(Author::getName)
                .collect(Collectors.toSet());

        return BookResponse.builder()
                .bookId(book.getBookId())
                .title(book.getTitle())
                .isbn(book.getIsbn())
                .edition(book.getEdition())
                .pages(book.getPages())
                .description(book.getDescription())
                .publicationYear(book.getPublicationYear())
                .language(book.getLanguage())
                .categoryId(book.getCategory().getCategoryId())
                .categoryName(book.getCategory().getName())
                .publisherId(book.getPublisher().getPublisherId())
                .publisherName(book.getPublisher().getName())
                .authors(authorNames)
                .createdAt(book.getCreatedAt())
                .updatedAt(book.getUpdatedAt())
                .build();
    }
}
