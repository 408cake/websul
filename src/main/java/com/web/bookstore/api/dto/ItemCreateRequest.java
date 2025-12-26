package com.web.bookstore.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ItemCreateRequest(
        @NotBlank String title,
        @NotNull @Min(0) Integer price,
        @NotNull @Min(0) Integer stock
) {}
