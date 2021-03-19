package com.thomasdriscoll.pluto.lib.exceptions;

import org.springframework.http.HttpStatus;

public enum CategoryExceptionEnums {

    INVALID_CATEGORY_STATE(HttpStatus.BAD_REQUEST, "Category must have one and only one boolean be true ");

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
