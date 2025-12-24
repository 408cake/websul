package com.web.bookstore.api.dto;

import jakarta.validation.constraints.*;

public record ReviewRequest(
        @NotNull Long bookId,
        @NotNull Long userId,
        @NotNull @Min(1) @Max(5) Integer rating,
        @NotBlank @Size(max = 1000) String comment,
        @NotBlank @Size(max = 255) String content
) {}
