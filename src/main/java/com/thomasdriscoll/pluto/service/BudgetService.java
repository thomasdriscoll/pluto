package com.thomasdriscoll.pluto.service;

import com.thomasdriscoll.pluto.lib.dao.BudgetRepo;
import com.thomasdriscoll.pluto.lib.exceptions.DriscollException;
import com.thomasdriscoll.pluto.lib.exceptions.BudgetExceptionEnums;
import org.springframework.stereotype.Service;

@Service
public class BudgetService {
    public BudgetService(BudgetRepo budgetRepo){}

    public String dummyFunction(String name) throws DriscollException {
        if(name.equals("Thummus")){
            throw new DriscollException(BudgetExceptionEnums.TESTING_EXCEPTIONS.getStatus(), BudgetExceptionEnums.TESTING_EXCEPTIONS.getMessage());
        }
        return "My name is " + name;
    }
}
