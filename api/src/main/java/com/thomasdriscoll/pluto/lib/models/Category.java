package com.thomasdriscoll.pluto.lib.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Category {
    String categoryName;
    String parentCategory;
    Boolean isNeed;
    Boolean isWant;
    Boolean isSavings;
    Boolean isIncome;
    Double dollarAmount;
    Integer periodicity;     // Frequency between payments (i.e. 1 == monthly)
}
