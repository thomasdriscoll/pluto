package com.thomasdriscoll.pluto.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thomasdriscoll.pluto.lib.exceptions.BudgetExceptionEnums;
import com.thomasdriscoll.pluto.lib.exceptions.CategoryExceptionEnums;
import com.thomasdriscoll.pluto.lib.exceptions.DriscollException;
import com.thomasdriscoll.pluto.lib.models.Category;
import com.thomasdriscoll.pluto.lib.responses.DriscollResponse;
import com.thomasdriscoll.pluto.service.BudgetService;
import com.thomasdriscoll.pluto.service.CategoryService;
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
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(CategoryController.class)
class CategoryControllerTest {

    @MockBean
    private CategoryService categoryService;

    @Autowired
    private MockMvc mockMvc;

    //Global testing variables
    private String USER_ID = "userId";

    private Category TEST = Category.builder().
            categoryName("Books").
            parentCategory("Shopping").
            isNeed(false).
            isWant(true).
            isSavings(false).
            isIncome(false).
            dollarAmount(50.0).
            periodicity(1).
            build();

    private Category NO_PARENT = Category.builder().
            categoryName("Books").
            isNeed(false).
            isWant(true).
            isSavings(false).
            isIncome(false).
            dollarAmount(50.0).
            periodicity(1).
            build();

    private Category BAD_CATEGORY = Category.builder().
            categoryName("Books").
            isNeed(false).
            isWant(true).
            isSavings(false).
            isIncome(true).     //Booleans are mutually exclusive
            dollarAmount(50.0).
            periodicity(1).
            build();


    @Nested
    @DisplayName("Create Category method tests")
    class CreateCategory_tests{
        String url = String.format("/users/%s/category", USER_ID);

        @Test
        public void givenUserIdAndCategory_whenCreateCategory_thenReturnResponseEntity() throws Exception {
            // Declare expected response and other variables used only in this test
            String request = new ObjectMapper().writeValueAsString(TEST);
            String expected = new ObjectMapper().writeValueAsString(new DriscollResponse<>(HttpStatus.CREATED.value(), TEST));

            //Mock what needs to be mocked
            when(categoryService.createCategory(USER_ID, TEST)).thenReturn(TEST);

            //Do test
            mockMvc.perform(post(url)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(request))
                    .andExpect(status().isCreated())
                    .andExpect(content().json(expected))
                    .andReturn();
        }

        @Test
        public void givenUserIdAndCategoryWithNoParent_whenCreateCategory_thenReturnResponseEntity() throws Exception{
            String request = new ObjectMapper().writeValueAsString(NO_PARENT);
            String expected = new ObjectMapper().writeValueAsString(new DriscollResponse<>(HttpStatus.CREATED.value(), NO_PARENT));

            //Mock what needs to be mocked
            when(categoryService.createCategory(USER_ID, NO_PARENT)).thenReturn(NO_PARENT);

            //Do test
            mockMvc.perform(post(url)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(request))
                    .andExpect(status().isCreated())
                    .andExpect(content().json(expected))
                    .andReturn();
        }

        @Test
        public void givenUserIdAndInvalidCategory_whenCreateCategory_thenThrowDriscollException() throws Exception{
            //Variables local to test
            String request = new ObjectMapper().writeValueAsString(BAD_CATEGORY);
            DriscollException exception = new DriscollException(CategoryExceptionEnums.INVALID_CATEGORY_STATE.getStatus(), CategoryExceptionEnums.INVALID_CATEGORY_STATE.getMessage());
            String expected = new ObjectMapper().writeValueAsString(new DriscollResponse<>(exception.getStatus().value(), exception.getMessage()));

            //Mock what needs to be mocked
            when(categoryService.createCategory(USER_ID, BAD_CATEGORY)).thenThrow(exception);

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
    @DisplayName("Update Category method tests")
    class UpdateCategory_tests{
        String url = String.format("/users/%s/category", USER_ID);

        @Test
        public void givenUserIdAndCategory_whenUpdateCategory_thenReturnResponseEntity() throws Exception {
            // Declare expected response and other variables used only in this test
            String request = new ObjectMapper().writeValueAsString(TEST);
            String expected = new ObjectMapper().writeValueAsString(new DriscollResponse<>(HttpStatus.OK.value(), TEST));

            //Mock what needs to be mocked
            when(categoryService.updateCategory(USER_ID, TEST)).thenReturn(TEST);

            //Do test
            mockMvc.perform(put(url)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(request))
                    .andExpect(status().isOk())
                    .andExpect(content().json(expected))
                    .andReturn();
        }

        @Test
        public void givenUserIdAndCategoryWithNoParent_whenUpdateCategory_thenReturnResponseEntity() throws Exception{
            String request = new ObjectMapper().writeValueAsString(NO_PARENT);
            String expected = new ObjectMapper().writeValueAsString(new DriscollResponse<>(HttpStatus.OK.value(), NO_PARENT));

            //Mock what needs to be mocked
            when(categoryService.updateCategory(USER_ID, NO_PARENT)).thenReturn(NO_PARENT);

            //Do test
            mockMvc.perform(put(url)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(request))
                    .andExpect(status().isOk())
                    .andExpect(content().json(expected))
                    .andReturn();
        }

        @Test
        public void givenUserIdAndInvalidCategory_whenUpdateCategory_thenThrowDriscollException() throws Exception{
            //Variables local to test
            String request = new ObjectMapper().writeValueAsString(BAD_CATEGORY);
            DriscollException exception = new DriscollException(CategoryExceptionEnums.INVALID_CATEGORY_STATE.getStatus(), CategoryExceptionEnums.INVALID_CATEGORY_STATE.getMessage());
            String expected = new ObjectMapper().writeValueAsString(new DriscollResponse<>(exception.getStatus().value(), exception.getMessage()));

            //Mock what needs to be mocked
            when(categoryService.updateCategory(USER_ID, BAD_CATEGORY)).thenThrow(exception);

            //Do and assert test
            mockMvc.perform(put(url)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(request))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().json(expected))
                    .andReturn();
        }

        @Test
        public void givenUserIdAndNonExistentCategory_whenUpdateCategory_thenThrowDriscollException() throws Exception{
            //Variables local to test
            String request = new ObjectMapper().writeValueAsString(TEST); // TEST is a good category that does not exist
            DriscollException exception = new DriscollException(CategoryExceptionEnums.CATEGORY_NOT_FOUND.getStatus(), CategoryExceptionEnums.CATEGORY_NOT_FOUND.getMessage());
            String expected = new ObjectMapper().writeValueAsString(new DriscollResponse<>(exception.getStatus().value(), exception.getMessage()));

            //Mock what needs to be mocked
            when(categoryService.updateCategory(USER_ID, TEST)).thenThrow(exception);

            //Do and assert test
            mockMvc.perform(put(url)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(request))
                    .andExpect(status().isNotFound())
                    .andExpect(content().json(expected))
                    .andReturn();
        }

    }

    @Nested
    @DisplayName("Delete Category method tests")
    class DeleteCategory_tests{
        String url = String.format("/users/%s/category/%s", USER_ID, TEST.getCategoryName());

        @Test
        public void givenUserIdAndCategory_whenDeleteCategory_thenReturnResponseEntity() throws Exception {
            // Declare expected response and other variables used only in this test
            String expected = new ObjectMapper().writeValueAsString(new DriscollResponse<>(HttpStatus.OK.value(), null));

            //Do test
            mockMvc.perform(delete(url)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNoContent())
                    .andExpect(content().json(expected))
                    .andReturn();

            verify(categoryService, times(1)).deleteCategory(USER_ID, TEST.getCategoryName());

        }

        @Test
        public void givenUserIdAndCategoryWithNoParent_whenDeleteCategory_thenReturnResponseEntity() throws Exception{
            String expected = new ObjectMapper().writeValueAsString(new DriscollResponse<>(HttpStatus.OK.value(), null));

            //Mock what needs to be mocked

            //Do test
            mockMvc.perform(delete(url)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNoContent())
                    .andExpect(content().json(expected))
                    .andReturn();

            verify(categoryService, times(1)).deleteCategory(USER_ID, NO_PARENT.getCategoryName());

        }

        @Test
        public void givenUserIdAndInvalidCategory_whenDeleteCategory_thenThrowDriscollException() throws Exception{
            //Variables local to test
            DriscollException exception = new DriscollException(CategoryExceptionEnums.INVALID_CATEGORY_STATE.getStatus(), CategoryExceptionEnums.INVALID_CATEGORY_STATE.getMessage());
            String expected = new ObjectMapper().writeValueAsString(new DriscollResponse<>(exception.getStatus().value(), exception.getMessage()));

            //Mock what needs to be mocked
            doThrow(exception).when(categoryService).deleteCategory(USER_ID, TEST.getCategoryName());

            //Do and assert test
            mockMvc.perform(delete(url)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().json(expected))
                    .andReturn();
        }

        @Test
        public void givenUserIdAndNonExistentCategory_whenDeleteCategory_thenThrowDriscollException() throws Exception{
            //Variables local to test
            DriscollException exception = new DriscollException(CategoryExceptionEnums.CATEGORY_NOT_FOUND.getStatus(), CategoryExceptionEnums.CATEGORY_NOT_FOUND.getMessage());
            String expected = new ObjectMapper().writeValueAsString(new DriscollResponse<>(exception.getStatus().value(), exception.getMessage()));

            //Mock what needs to be mocked
            doThrow(exception).when(categoryService).deleteCategory(USER_ID, TEST.getCategoryName());

            //Do and assert test
            mockMvc.perform(delete(url)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(content().json(expected))
                    .andReturn();
        }

    }

}