package com.web.bookstore.api.dto;

public record BookSummary(
        Long id,
        String title,
        Integer price
) { }
