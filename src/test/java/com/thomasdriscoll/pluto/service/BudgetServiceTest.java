package com.thomasdriscoll.pluto.service;

import com.thomasdriscoll.pluto.lib.dao.BudgetRepo;
import com.thomasdriscoll.pluto.lib.exceptions.DriscollException;
import com.thomasdriscoll.pluto.lib.exceptions.BudgetExceptionEnums;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class BudgetServiceTest {
    //Complete dummy variable but just wanted to highlight that you'd probably have one of these in your service tests
    @MockBean
    private BudgetRepo budgetRepo;

    @Autowired
    private BudgetService budgetService;
}