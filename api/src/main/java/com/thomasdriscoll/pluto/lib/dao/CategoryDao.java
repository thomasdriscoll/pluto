package com.thomasdriscoll.pluto.lib.dao;

import com.thomasdriscoll.pluto.lib.models.Category;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name="t_budget_category")
public class CategoryDao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="category_id")
    private Long id;

    @Column(name="category_name") String categoryName;

    @Column(name="parent_category") String parentCategory;

    @Column(name="is_need") Boolean isNeed;

    @Column(name="is_want") Boolean isWant;

    @Column(name="is_savings") Boolean isSavings;

    @Column(name="is_income") Boolean isIncome;

    @Column(name="dollar_amount") Double dollarAmount;


    public CategoryDao(Category category){
        this.categoryName = category.getCategoryName();
        this.parentCategory = category.getParentCategory();
        this.isNeed = category.getIsNeed();
        this.isWant = category.getIsWant();
        this.isSavings = category.getIsSavings();
        this.isIncome = category.getIsIncome();
        this.dollarAmount = category.getDollarAmount();
    }

    public Category toCategory(){
        return Category.builder()
                .categoryName(this.categoryName)
                .parentCategory(this.parentCategory)
                .isNeed(this.isNeed)
                .isWant(this.isWant)
                .isSavings(this.isSavings)
                .isIncome(this.isIncome)
                .dollarAmount(this.dollarAmount)
                .build();
    }



}
