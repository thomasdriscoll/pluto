package com.thomasdriscoll.pluto.service;

import com.thomasdriscoll.pluto.lib.dao.BudgetRepo;
import com.thomasdriscoll.pluto.lib.enums.BudgetExceptionEnums;
import com.thomasdriscoll.pluto.lib.exceptions.DriscollException;
import com.thomasdriscoll.pluto.lib.models.Budget;
import com.thomasdriscoll.pluto.lib.models.BudgetRequest;
import com.thomasdriscoll.pluto.lib.enums.BudgetType;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class BudgetService {
    private final BudgetRepo budgetRepo;
    private final CategoryService categoryService;

    public BudgetService(BudgetRepo budgetRepo, CategoryService categoryService){
        this.budgetRepo = budgetRepo;
        this.categoryService = categoryService;
    }

    // Main functionality
    public Budget createBudget(String userId, BudgetRequest request) throws DriscollException {
        //validate inputs
        validateUser(userId);
        validateRequest(request);

        //Create budget
        Budget budget;

        // Determine budget strategy
        BudgetType BUDGET_TYPE = BudgetType.valueOf(request.getBudgetType());
        switch(BUDGET_TYPE) {
            case STABLE_INCOME:
                budget = stableIncome(userId, request);
                break;
            default:    // Identical to stableIncome right now
                budget = defaultBudget(userId, request);
                break;
        }
        return budget;
    }

    public Budget getBudget(String userId) throws DriscollException{
        //validate user
        validateUser(userId);
        return new Budget(new ArrayList<>());
    }

    //Budget strategies
    private Budget stableIncome(String userId, BudgetRequest request) throws DriscollException {

        return new Budget(new ArrayList<>());
    }

    private Budget defaultBudget(String userId, BudgetRequest request) throws DriscollException {
        return new Budget(new ArrayList<>());
    }

    // Validation functions
    private void validateUser(String userId) throws DriscollException {
        //To do pending completion of Andro
    }

    private void validateRequest(BudgetRequest request) throws DriscollException{
        if(request.income < 0) {
            throw new DriscollException(BudgetExceptionEnums.INVALID_INCOME.getStatus(), BudgetExceptionEnums.INVALID_INCOME.getMessage());
        }
        if(request.budgetType == null || request.budgetType.equals("")){
            throw new DriscollException(BudgetExceptionEnums.INVALID_BUDGET_TYPE.getStatus(), BudgetExceptionEnums.INVALID_BUDGET_TYPE.getMessage());
        }

        try {
            BudgetType validRequest = BudgetType.valueOf(request.getBudgetType());
        } catch (Exception e) {
            throw new DriscollException(BudgetExceptionEnums.INVALID_BUDGET_TYPE.getStatus(), BudgetExceptionEnums.INVALID_BUDGET_TYPE.getMessage());
        }
    }
}
