package com.thomasdriscoll.pluto.lib.enums;

public enum DefaultCategories {
    // Needs -- 50%
    RENT("Rent", "Home", "isNeed", 0.3333),
    GROCERIES("Groceries", "Food", "isNeed", 0.05),
    UTILITIES("Utilities", "Home", "isNeed", 0.03333),
    PHONE("Phone", "", "isNeed", 0.1),
    GAS("Gas", "Transport", "isNeed", .01),
    EDUCATION("Education", "", "isNeed", 0.1),
    FOOD("Food", "", "isNeed", 0.1),
    // Debts
    STUDENT_LOAN("Student Loans", "Debt", "isNeed", .01),
    CAR_PAYMENT("Car Payment", "Debt", "isNeed", .01),
    // Insurances
    HEALTH_INSURANCE("Health Insurance", "Insurance", "isNeed", 0.1),
    CAR_INSURANCE("Car Insurance", "Insurance", "isNeed", 0.1),

    // Wants -- 20%
    SHOPPING("", "", "isWant", 0.15),
    HAIR_CUT("Hair Cut", "Shopping", "isWant", 0.1),
    GIFTS("Gifts", "Gifts and Charity", "isWant", 0.025),
    RESTAURANTS("Restaurants", "Food", "isWant", 0.05),

    // Savings -- 30%
    RETIREMENT("Retirement", "Savings", "isSavings", 0.15),
    INVESTMENT("Investment", "Savings", "isSavings", 0.10),
    EMERGENCIES("Rainy Day Fund", "Savings", "isSavings", 0.05) //Note: We'll be aiming to have 6 months after-tax income saved
    ;

    private String categoryName;
    private String parentName;
    private String categoryType;
    private double percentage;

    DefaultCategories(
        String categoryName,
        String parentName,
        String categoryType,
        double percentage
    ){
        this.categoryName = categoryName;
        this.parentName = parentName;
        this.categoryType = categoryType;
        this.percentage = percentage;
    }

    public String getCategoryName() { return this.categoryName; }
    public String getParentName() { return this.parentName; }
    public String getCategoryType() { return this.categoryType; }
    public double getPercentage() { return this.percentage; }
}
