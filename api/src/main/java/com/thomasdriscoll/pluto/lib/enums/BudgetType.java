package com.thomasdriscoll.pluto.lib.enums;

import lombok.Data;

public enum BudgetType {
    STABLE_INCOME("STABLE_INCOME");

    private final String value;

    BudgetType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
