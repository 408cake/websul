package com.web.bookstore.common;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    INVALID_REQUEST_BODY(HttpStatus.BAD_REQUEST, "요청 본문 형식이 잘못됨"),
    INVALID_QUERY_PARAM(HttpStatus.BAD_REQUEST, "쿼리 파라미터 값이 잘못됨"),

    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 이메일"),

    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "이메일 또는 비밀번호가 올바르지 않음"),

    ACCESS_TOKEN_MISSING(HttpStatus.UNAUTHORIZED, "인증 토큰이 필요함"),
    ACCESS_TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "유효하지 않은 인증 토큰"),

    REFRESH_TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "유효하지 않은 리프레시 토큰"),
    REFRESH_TOKEN_REVOKED(HttpStatus.UNAUTHORIZED, "폐기된 리프레시 토큰"),

    FORBIDDEN(HttpStatus.FORBIDDEN, "접근 권한이 없음"),

    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "리소스를 찾을 수 없음"),

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류");

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public HttpStatus status() {
        return status;
    }

    public String message() {
        return message;
    }
}
