package com.libraryhub.catalog.dto;

import lombok.Data;

import java.util.Set;

@Data
public class BookRequest {

    private String title;
    private String isbn;
    private String edition;
    private Integer pages;
    private String description;
    private Integer publicationYear;
    private String language;

    private Long categoryId;
    private Long publisherId;
    private Set<Long> authorIds;
}
