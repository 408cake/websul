package com.web.bookstore.api.dto;

import jakarta.validation.constraints.Min;

public record ItemUpdateRequest(
        String title,
        @Min(0) Integer price,
        @Min(0) Integer stock
) {}
