package com.web.bookstore.common;

import java.time.OffsetDateTime;

public record ApiResponse<T>(
        boolean success,
        OffsetDateTime timestamp,
        T data
) {
    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(true, OffsetDateTime.now(), data);
    }
}
