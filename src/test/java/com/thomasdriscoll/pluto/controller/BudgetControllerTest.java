package com.thomasdriscoll.pluto.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thomasdriscoll.pluto.lib.exceptions.DriscollException;
import com.thomasdriscoll.pluto.lib.enums.BudgetExceptionEnums;
import com.thomasdriscoll.pluto.lib.models.Budget;
import com.thomasdriscoll.pluto.lib.models.BudgetRequest;
import com.thomasdriscoll.pluto.lib.models.Category;
import com.thomasdriscoll.pluto.lib.responses.DriscollResponse;
import com.thomasdriscoll.pluto.service.BudgetService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;


import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(BudgetController.class)
public class BudgetControllerTest {
    @MockBean
    private BudgetService budgetService;

    @Autowired
    private MockMvc mockMvc;

    //Testing variables
    private final String USER_ID = "userId";

    private final Category TEST = Category.builder().
            categoryName("Books").
            parentCategory("Shopping").
            isNeed(false).
            isWant(true).
            isSavings(false).
            isIncome(false).
            dollarAmount(50.0).
            build();

    private final Category NO_PARENT = Category.builder().
            categoryName("Books").
            isNeed(false).
            isWant(true).
            isSavings(false).
            isIncome(false).
            dollarAmount(50.0).
            build();

    private final Double INCOME = 50000.0;
    private final Integer ZIP = 12345;
    private final String BUDGET_TYPE = "STABLE_INCOME";
    private final BudgetRequest REQUEST = new BudgetRequest(INCOME, ZIP, BUDGET_TYPE);
    private final BudgetRequest NO_ZIP_REQUEST = new BudgetRequest(INCOME, null, BUDGET_TYPE);
    private final BudgetRequest BAD_REQUEST = new BudgetRequest(INCOME, ZIP, null);
    private final Budget BUDGET = new Budget(new ArrayList<>(Arrays.asList(TEST, NO_PARENT)));

    //Give each endpoint its own class of testing functions, so its easy to see what works and what doesn't
    @Nested
    @DisplayName("Create Budget method tests")
    class createBudget_tests{
        String url = String.format("/users/%s/budget", USER_ID);

        @Test
        public void givenUserIdAndBudgetRequest_whenCreateBudget_thenReturnResponseEntity() throws Exception {
            // Declare expected response and other variables used only in this test
            String request = new ObjectMapper().writeValueAsString(REQUEST);
            String expected = new ObjectMapper().writeValueAsString(new DriscollResponse<>(HttpStatus.CREATED.value(), BUDGET));

            //Mock what needs to be mocked
            when(budgetService.createBudget(USER_ID, REQUEST)).thenReturn(BUDGET);

            //Do test
            mockMvc.perform(post(url)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(request))
                    .andExpect(status().isCreated())
                    .andExpect(content().json(expected))
                    .andReturn();
        }

        @Test
        public void givenUserIdAndCategoryWithNoZip_whenCreateBudget_thenReturnResponseEntity() throws Exception{
            String request = new ObjectMapper().writeValueAsString(NO_ZIP_REQUEST);
            String expected = new ObjectMapper().writeValueAsString(new DriscollResponse<>(HttpStatus.CREATED.value(), BUDGET));

            //Mock what needs to be mocked
            when(budgetService.createBudget(USER_ID, REQUEST)).thenReturn(BUDGET);

            //Do test
            mockMvc.perform(post(url)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(request))
                    .andExpect(status().isCreated())
                    .andExpect(content().json(expected))
                    .andReturn();
        }

        @Test
        public void givenUserIdAndInvalidBudgetType_whenCreateBudget_thenThrowDriscollException() throws Exception{
            //Variables local to test
            String request = new ObjectMapper().writeValueAsString(BAD_REQUEST);
            DriscollException exception = new DriscollException(BudgetExceptionEnums.INVALID_BUDGET_TYPE.getStatus(), BudgetExceptionEnums.INVALID_BUDGET_TYPE.getMessage());
            String expected = new ObjectMapper().writeValueAsString(new DriscollResponse<>(exception.getStatus().value(), exception.getMessage()));

            //Mock what needs to be mocked
            when(budgetService.createBudget(USER_ID, REQUEST)).thenThrow(exception);

            //Do and assert test
            mockMvc.perform(post(url)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(request))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().json(expected))
                    .andReturn();
        }
    }

    @Nested
    @DisplayName("Get Budget tests")
    class GetBudgetTests{
        String url = String.format("/users/%s/budget", USER_ID);

        @Test
        public void givenUserId_whenGetBudget_thenReturnBudget() throws Exception {
            String expected = new ObjectMapper().writeValueAsString(new DriscollResponse<>(HttpStatus.OK.value(), BUDGET));

            //Mock what needs to be mocked
            when(budgetService.getBudget(USER_ID)).thenReturn(BUDGET);

            //Do test
            mockMvc.perform(get(url)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().json(expected))
                    .andReturn();
        }

        @Test
        public void givenInvalidUserId_whenGetBudget_thenThrowDriscollException() throws Exception{
            //Variables local to test
            DriscollException exception = new DriscollException(BudgetExceptionEnums.USER_NOT_FOUND.getStatus(), BudgetExceptionEnums.USER_NOT_FOUND.getMessage());
            String expected = new ObjectMapper().writeValueAsString(new DriscollResponse<>(exception.getStatus().value(), exception.getMessage()));

            //Mock what needs to be mocked
            when(budgetService.getBudget(USER_ID)).thenThrow(exception);

            //Do and assert test
            mockMvc.perform(get(url)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(content().json(expected))
                    .andReturn();
        }

        @Test
        public void givenValidUserIdButNoExistingBudget_whenGetBudget_thenThrowDriscollException() throws Exception{
            //Variables local to test
            DriscollException exception = new DriscollException(BudgetExceptionEnums.BUDGET_NOT_FOUND.getStatus(), BudgetExceptionEnums.BUDGET_NOT_FOUND.getMessage());
            String expected = new ObjectMapper().writeValueAsString(new DriscollResponse<>(exception.getStatus().value(), exception.getMessage()));

            //Mock what needs to be mocked
            when(budgetService.getBudget(USER_ID)).thenThrow(exception);

            //Do and assert test
            mockMvc.perform(get(url)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(content().json(expected))
                    .andReturn();
        }
    }
}