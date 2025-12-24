package com.web.bookstore.common;

import java.time.OffsetDateTime;

public record ErrorResponse(
        OffsetDateTime timestamp,
        String path,
        int status,
        String code,
        String message,
        Object details
) {}
