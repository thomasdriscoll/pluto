package com.thomasdriscoll.pluto.lib.models;

import lombok.Data;

@Data
public class BudgetRequest {
    public String userId;
    public Double income;
    public Integer zipCode;
    public String budgetType;

    public BudgetRequest(String user_id, Double income, Integer zip, String budgetType) {
        this.userId = user_id;
        this.income = income;
        this.zipCode = zip;
        this.budgetType = budgetType;
    }
}
