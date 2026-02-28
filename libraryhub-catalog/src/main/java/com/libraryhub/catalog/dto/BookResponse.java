package com.libraryhub.catalog.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
public class BookResponse {

    private Long bookId;
    private String title;
    private String isbn;
    private String edition;
    private Integer pages;
    private String description;
    private Integer publicationYear;
    private String language;

    private Long categoryId;
    private String categoryName;

    private Long publisherId;
    private String publisherName;

    private Set<String> authors;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
