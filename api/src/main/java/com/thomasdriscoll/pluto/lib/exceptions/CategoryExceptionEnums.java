package com.thomasdriscoll.pluto.lib.exceptions;

import org.springframework.http.HttpStatus;

public enum CategoryExceptionEnums {
    // 400 errors
    INVALID_CATEGORY_STATE(HttpStatus.BAD_REQUEST, "Category must have one and only one boolean be true "),
    // 404 errors
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "Category does not exist for user");


    private final HttpStatus status;
    private final String message;

    CategoryExceptionEnums(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
