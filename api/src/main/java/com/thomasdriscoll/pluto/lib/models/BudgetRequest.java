package com.thomasdriscoll.pluto.lib.models;

import lombok.Data;

@Data
public class BudgetRequest {
    public Double income;
    public Integer zipCode;
    public String budgetType;

    public BudgetRequest(Double income, Integer zip, String budgetType) {
        this.income = income;
        this.zipCode = zip;
        this.budgetType = budgetType;
        //Types: stable_income
    }
}
