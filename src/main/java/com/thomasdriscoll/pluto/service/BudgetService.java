package com.thomasdriscoll.pluto.service;

import com.thomasdriscoll.pluto.lib.dao.BudgetRepo;
import com.thomasdriscoll.pluto.lib.exceptions.DriscollException;
import com.thomasdriscoll.pluto.lib.models.Budget;
import com.thomasdriscoll.pluto.lib.models.BudgetRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class BudgetService {
    public BudgetService(BudgetRepo budgetRepo){}

    public Budget createBudget(BudgetRequest request) throws DriscollException {
        return new Budget(new ArrayList<>());
    }
}
