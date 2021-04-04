package com.thomasdriscoll.pluto.service;

import com.thomasdriscoll.pluto.lib.dao.BudgetRepo;
import com.thomasdriscoll.pluto.lib.enums.BudgetExceptionEnums;
import com.thomasdriscoll.pluto.lib.enums.BudgetType;
import com.thomasdriscoll.pluto.lib.enums.DefaultCategories;
import com.thomasdriscoll.pluto.lib.exceptions.DriscollException;
import com.thomasdriscoll.pluto.lib.models.Budget;
import com.thomasdriscoll.pluto.lib.models.BudgetRequest;
import com.thomasdriscoll.pluto.lib.models.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest
class BudgetServiceTest {
    //Complete dummy variable but just wanted to highlight that you'd probably have one of these in your service tests
    @MockBean
    private BudgetRepo budgetRepo;

    @MockBean
    private CategoryService categoryService;

    @Autowired
    private BudgetService budgetService;

    //Testing variables
    private String USER_ID = "userId";
    private Double INCOME = 50000.0;
    private Double BAD_INCOME = -50000.0;
    private Integer ZIP = 12345;
    private BudgetRequest REQUEST = new BudgetRequest(INCOME, ZIP, BudgetType.STABLE_INCOME.getValue());
    private BudgetRequest BAD_INCOME_REQUEST = new BudgetRequest(BAD_INCOME, ZIP, BudgetType.STABLE_INCOME.getValue());
    private BudgetRequest BAD_TYPE_REQUEST = new BudgetRequest(INCOME, ZIP, "garbage_in");

    @Nested
    @DisplayName("Create Budget tests")
    class CreateBudgetTests {
        @BeforeEach
        public void setup(){

        }

        @Nested
        @DisplayName("Validating user and request test")
        class GeneralValidationTests{
            @Test
            public void givenBadIncome_whenCreateBudget_thenThrowDriscoll() throws DriscollException {
                DriscollException excepted = new DriscollException(BudgetExceptionEnums.INVALID_INCOME.getStatus(), BudgetExceptionEnums.INVALID_INCOME.getMessage());

                // Note: AssertEquals does a deep assertion, i.e. it is testing if the objects are literally the same object in memory. Easiest way around this is to test contents
                // Good enough for our purposes
                DriscollException actual = assertThrows(DriscollException.class, () -> budgetService.createBudget(USER_ID, BAD_INCOME_REQUEST));
                assertEquals(excepted.getStatus(), actual.getStatus());
                assertEquals(excepted.getMessage(), actual.getMessage());
            }

            @Test
            public void givenNoType_whenCreateBudget_thenThrowDriscoll() throws DriscollException {
                BudgetRequest NO_TYPE_REQUEST = new BudgetRequest(INCOME, ZIP, null);
                DriscollException excepted = new DriscollException(BudgetExceptionEnums.INVALID_BUDGET_TYPE.getStatus(), BudgetExceptionEnums.INVALID_BUDGET_TYPE.getMessage());
                DriscollException actual = assertThrows(DriscollException.class, () -> budgetService.createBudget(USER_ID, NO_TYPE_REQUEST));

                assertEquals(excepted.getStatus(), actual.getStatus());
                assertEquals(excepted.getMessage(), actual.getMessage());
            }

            @Test
            public void givenBadType_whenCreateBudget_thenThrowDriscoll() throws DriscollException {
                DriscollException excepted = new DriscollException(BudgetExceptionEnums.INVALID_BUDGET_TYPE.getStatus(), BudgetExceptionEnums.INVALID_BUDGET_TYPE.getMessage());
                DriscollException actual = assertThrows(DriscollException.class, () -> budgetService.createBudget(USER_ID, BAD_TYPE_REQUEST));

                assertEquals(excepted.getStatus(), actual.getStatus());
                assertEquals(excepted.getMessage(), actual.getMessage());
            }
        }

        @Nested
        @DisplayName("Stable Income test")
        class DefaultBudgetTests{
            @BeforeEach
            public void setup() throws DriscollException {
                when(categoryService.createCategory(any(), any())).thenReturn(Category.builder().build());
            }
            @Test
            public void givenUserIdAndValidRequest_whenCreateBudget_thenReturnBudgetWithCorrectNumberOfCategories() throws DriscollException {
                int numberOfCategoriesExpected = DefaultCategories.values().length +1; //+1 for income
                Budget budget = budgetService.createBudget(USER_ID, REQUEST);
                assertEquals(budget.getCategories().size(), numberOfCategoriesExpected);
            }
            @Test
            public void givenUserIdAndValidRequest_whenCreateBudget_thenThrowCategoryException() throws DriscollException {

            }
        }
    }
}