package com.thomasdriscoll.pluto.lib.enums;

import org.springframework.http.HttpStatus;

//This is just a sample enum for exceptions; delete!
public enum BudgetExceptionEnums {
    INVALID_BUDGET_TYPE(HttpStatus.BAD_REQUEST, "Budget type is invalid"),
    INVALID_INCOME(HttpStatus.BAD_REQUEST, "Income must be 0 or positive"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User not found"),
    BUDGET_NOT_FOUND(HttpStatus.NOT_FOUND, "Budget not found");

    private final HttpStatus status;
    private final String message;

    BudgetExceptionEnums(HttpStatus status, String message) {
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
