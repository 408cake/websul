package com.web.bookstore.api.dto;

import jakarta.validation.constraints.NotBlank;

public record FirebaseLoginRequest(
        @NotBlank String idToken
) {}
