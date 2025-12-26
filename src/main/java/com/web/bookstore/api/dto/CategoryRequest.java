package com.web.bookstore.api.dto;

import jakarta.validation.constraints.NotBlank;

public record CategoryRequest(
        @NotBlank String name,
        Long parentId
) {}
