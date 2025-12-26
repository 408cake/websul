package com.web.bookstore.api.dto;

import java.math.BigDecimal;

public record BookSummary(
        Long id,
        String title,
        BigDecimal price
) {}
