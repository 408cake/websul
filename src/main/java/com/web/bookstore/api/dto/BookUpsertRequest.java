package com.web.bookstore.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

public record BookUpsertRequest(
        @NotNull Long sellerId,
        @NotBlank String title,
        @NotBlank String isbn,
        String publisher,
        String summary,
        @NotNull BigDecimal price,
        LocalDate publicationDate,
        @NotNull @Min(0) Integer inventory
) {}
