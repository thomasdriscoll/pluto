package com.thomasdriscoll.pluto.service;

import com.thomasdriscoll.pluto.lib.dao.CategoryDao;
import com.thomasdriscoll.pluto.lib.dao.CategoryRepository;
import com.thomasdriscoll.pluto.lib.exceptions.CategoryExceptionEnums;
import com.thomasdriscoll.pluto.lib.exceptions.DriscollException;
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

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class CategoryServiceTest {
    @MockBean
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryService categoryService;

    private final String USER_ID = "userId";
    private final String BAD_USER_ID = "badUserId";

    private Category TEST = Category.builder().
            categoryName("Books").
            parentCategory("Shopping").
            isNeed(false).
            isWant(true).
            isSavings(false).
            isIncome(false).
            dollarAmount(50.0).
            build();

    private Category UPDATE_TEST = Category.builder().
            categoryName("Books").
            parentCategory("Shopping").
            isNeed(true).
            isWant(false).
            isSavings(false).
            isIncome(false).
            dollarAmount(100.0).
            build();

    private Category NO_PARENT = Category.builder().
            categoryName("Shopping").
            isNeed(false).
            isWant(true).
            isSavings(false).
            isIncome(false).
            dollarAmount(50.0).
            build();

    private Category BAD_CATEGORY = Category.builder().
            categoryName("Gibberish").
            isNeed(false).
            isWant(true).
            isSavings(false).
            isIncome(true).     //Booleans are mutually exclusive
            dollarAmount(50.0).
            build();

    private CategoryDao DAO = new CategoryDao(TEST);
    private CategoryDao NO_PARENT_DAO = new CategoryDao(NO_PARENT);
    private CategoryDao UPDATE_DAO = new CategoryDao(UPDATE_TEST);
    private List<CategoryDao> LIST_DAO = new ArrayList<CategoryDao>(){{ add(DAO); add(NO_PARENT_DAO); }};

    //Default to happy path so we can test each error precisely
    @BeforeEach
    void setup(){
        when(categoryRepository.findByUserId(USER_ID)).thenReturn(LIST_DAO);
        when(categoryRepository.findByUserIdAndParentCategory(USER_ID, TEST.getParentCategory())).thenReturn(new ArrayList<CategoryDao>(){{add(DAO);}});
        when(categoryRepository.save(DAO)).thenReturn(DAO);
        when(categoryRepository.save(NO_PARENT_DAO)).thenReturn(NO_PARENT_DAO);
    }

    @Nested
    @DisplayName("Create Category Service tests")
    class CreateCategoryTests{
        @BeforeEach
        void setup(){
            when(categoryRepository.findByUserIdAndCategoryName(USER_ID, TEST.getCategoryName())).thenReturn(null);
            when(categoryRepository.findByUserIdAndCategoryName(USER_ID, NO_PARENT.getCategoryName())).thenReturn(null);
        }
        @Test
        public void whenValidUserIdAndCategory_returnCategory() throws DriscollException {
            Category actual = categoryService.createCategory(USER_ID, TEST);
            assertEquals(TEST, actual);
        }

        @Test
        public void whenValidUserIdAndNoParentCategory_returnCategory() throws DriscollException {
            Category actual = categoryService.createCategory(USER_ID, NO_PARENT);
            assertEquals(NO_PARENT, actual);
        }

        @Test
        public void whenInvalidUserIdAndValidCategory_throwDriscollException() throws DriscollException {
            DriscollException excepted = new DriscollException(CategoryExceptionEnums.INVALID_USER_ID.getStatus(), CategoryExceptionEnums.INVALID_USER_ID.getMessage());

            when(categoryRepository.findByUserId(BAD_USER_ID)).thenReturn(new ArrayList<CategoryDao>());

            // Note: AssertEquals does a deep assertion, i.e. it is testing if the objects are literally the same object in memory. Easiest way around this is to test contents
            // Good enough for our purposes
            DriscollException actual = assertThrows(DriscollException.class, () -> categoryService.createCategory(BAD_USER_ID, TEST));
            assertEquals(excepted.getStatus(), actual.getStatus());
            assertEquals(excepted.getMessage(), actual.getMessage());
        }

        @Test
        public void whenInvalidUserIdAndInValidCategory_throwDriscollExceptionOnUserId() throws DriscollException {
            DriscollException excepted = new DriscollException(CategoryExceptionEnums.INVALID_USER_ID.getStatus(), CategoryExceptionEnums.INVALID_USER_ID.getMessage());

            when(categoryRepository.findByUserId(BAD_USER_ID)).thenReturn(new ArrayList<CategoryDao>());

            DriscollException actual = assertThrows(DriscollException.class, () -> categoryService.createCategory(BAD_USER_ID, BAD_CATEGORY));
            assertEquals(excepted.getStatus(), actual.getStatus());
            assertEquals(excepted.getMessage(), actual.getMessage());
        }

        @Test
        public void whenValidUserIdAndInvalidCategoryByState_throwDriscollException() throws DriscollException {
            DriscollException excepted = new DriscollException(CategoryExceptionEnums.INVALID_CATEGORY_STATE.getStatus(), CategoryExceptionEnums.INVALID_CATEGORY_STATE.getMessage());
            DriscollException actual = assertThrows(DriscollException.class, () -> categoryService.createCategory(USER_ID, BAD_CATEGORY));


            assertEquals(excepted.getStatus(), actual.getStatus());
            assertEquals(excepted.getMessage(), actual.getMessage());
        }

        @Test
        public void whenValidUserIdAndInvalidCategoryByParent_throwDriscollException() throws DriscollException {
            DriscollException excepted = new DriscollException(CategoryExceptionEnums.INVALID_CATEGORY_PARENT.getStatus(), CategoryExceptionEnums.INVALID_CATEGORY_PARENT.getMessage());

            when(categoryRepository.findByUserIdAndParentCategory(USER_ID, TEST.getParentCategory())).thenReturn(new ArrayList<CategoryDao>());

            DriscollException actual = assertThrows(DriscollException.class, () -> categoryService.createCategory(USER_ID, TEST));


            assertEquals(excepted.getStatus(), actual.getStatus());
            assertEquals(excepted.getMessage(), actual.getMessage());
        }

        @Test
        public void whenValidUserIdAndInvalidCategoryByName_throwDriscollException() throws DriscollException {
            DriscollException excepted = new DriscollException(CategoryExceptionEnums.INVALID_CATEGORY_NAME.getStatus(), CategoryExceptionEnums.INVALID_CATEGORY_NAME.getMessage());

            when(categoryRepository.findByUserIdAndCategoryName(USER_ID, TEST.getCategoryName())).thenReturn(DAO);

            DriscollException actual = assertThrows(DriscollException.class, () -> categoryService.createCategory(USER_ID, TEST));


            assertEquals(excepted.getStatus(), actual.getStatus());
            assertEquals(excepted.getMessage(), actual.getMessage());
        }
    }

    @Nested
    @DisplayName("Update Category Service tests")
    class UpdateCategoryTests {
        @BeforeEach
        void setup(){
            when(categoryRepository.findByUserIdAndCategoryName(USER_ID, TEST.getCategoryName())).thenReturn(DAO);
        }

        @Test
        public void whenValidUserIdAndCategory_returnCategory() throws DriscollException {
            Category actual = categoryService.updateCategory(USER_ID, TEST);
            assertEquals(TEST, actual);
        }

        @Test
        public void whenInvalidUserIdAndValidCategory_throwDriscollException() throws DriscollException {
            DriscollException excepted = new DriscollException(CategoryExceptionEnums.INVALID_USER_ID.getStatus(), CategoryExceptionEnums.INVALID_USER_ID.getMessage());

            when(categoryRepository.findByUserId(BAD_USER_ID)).thenReturn(new ArrayList<CategoryDao>());

            // Note: AssertEquals does a deep assertion, i.e. it is testing if the objects are literally the same object in memory. Easiest way around this is to test contents
            // Good enough for our purposes
            DriscollException actual = assertThrows(DriscollException.class, () -> categoryService.updateCategory(BAD_USER_ID, TEST));
            assertEquals(excepted.getStatus(), actual.getStatus());
            assertEquals(excepted.getMessage(), actual.getMessage());
        }

        @Test
        public void whenInvalidUserIdAndInValidCategory_throwDriscollExceptionOnUserId() throws DriscollException {
            DriscollException excepted = new DriscollException(CategoryExceptionEnums.INVALID_USER_ID.getStatus(), CategoryExceptionEnums.INVALID_USER_ID.getMessage());

            when(categoryRepository.findByUserId(BAD_USER_ID)).thenReturn(new ArrayList<CategoryDao>());

            DriscollException actual = assertThrows(DriscollException.class, () -> categoryService.updateCategory(BAD_USER_ID, BAD_CATEGORY));
            assertEquals(excepted.getStatus(), actual.getStatus());
            assertEquals(excepted.getMessage(), actual.getMessage());
        }

        @Test
        public void whenValidUserIdAndInvalidCategoryByState_throwDriscollException() throws DriscollException {
            DriscollException excepted = new DriscollException(CategoryExceptionEnums.INVALID_CATEGORY_STATE.getStatus(), CategoryExceptionEnums.INVALID_CATEGORY_STATE.getMessage());
            DriscollException actual = assertThrows(DriscollException.class, () -> categoryService.updateCategory(USER_ID, BAD_CATEGORY));


            assertEquals(excepted.getStatus(), actual.getStatus());
            assertEquals(excepted.getMessage(), actual.getMessage());
        }

        @Test
        public void whenValidUserIdAndInvalidCategoryByParent_throwDriscollException() throws DriscollException {
            DriscollException excepted = new DriscollException(CategoryExceptionEnums.INVALID_CATEGORY_PARENT.getStatus(), CategoryExceptionEnums.INVALID_CATEGORY_PARENT.getMessage());

            when(categoryRepository.findByUserIdAndParentCategory(USER_ID, TEST.getParentCategory())).thenReturn(new ArrayList<CategoryDao>());

            DriscollException actual = assertThrows(DriscollException.class, () -> categoryService.updateCategory(USER_ID, TEST));


            assertEquals(excepted.getStatus(), actual.getStatus());
            assertEquals(excepted.getMessage(), actual.getMessage());
        }

        @Test
        public void whenValidUserIdAndInvalidCategoryByName_throwDriscollException() throws DriscollException {
            DriscollException excepted = new DriscollException(CategoryExceptionEnums.CATEGORY_NOT_FOUND.getStatus(), CategoryExceptionEnums.CATEGORY_NOT_FOUND.getMessage());

            when(categoryRepository.findByUserIdAndCategoryName(USER_ID, TEST.getCategoryName())).thenReturn(null);

            DriscollException actual = assertThrows(DriscollException.class, () -> categoryService.updateCategory(USER_ID, TEST));


            assertEquals(excepted.getStatus(), actual.getStatus());
            assertEquals(excepted.getMessage(), actual.getMessage());
        }
    }

    @Nested
    @DisplayName("Delete Category Service tests")
    class DeleteCategoryTests {
        @BeforeEach
        void setup(){
            when(categoryRepository.findByUserIdAndCategoryName(USER_ID, TEST.getCategoryName())).thenReturn(DAO);
            when(categoryRepository.findByUserIdAndCategoryName(USER_ID, NO_PARENT.getCategoryName())).thenReturn(NO_PARENT_DAO);
        }

        @Test
        public void whenValidUserIdAndCategory_returnCategory() throws DriscollException {
            categoryService.deleteCategory(USER_ID, TEST.getCategoryName());
            verify(categoryRepository).deleteByUserIdAndCategoryName(USER_ID, TEST.getCategoryName());
        }

        @Test
        public void whenValidUserIdAndNoParentCategory_returnCategory() throws DriscollException {
            categoryService.deleteCategory(USER_ID, NO_PARENT.getCategoryName());
            verify(categoryRepository).deleteByUserIdAndCategoryName(USER_ID, NO_PARENT.getCategoryName());
        }

        @Test
        public void whenInvalidUserIdAndValidCategory_throwDriscollException() throws DriscollException {
            DriscollException excepted = new DriscollException(CategoryExceptionEnums.INVALID_USER_ID.getStatus(), CategoryExceptionEnums.INVALID_USER_ID.getMessage());

            when(categoryRepository.findByUserId(BAD_USER_ID)).thenReturn(new ArrayList<CategoryDao>());

            // Note: AssertEquals does a deep assertion, i.e. it is testing if the objects are literally the same object in memory. Easiest way around this is to test contents
            // Good enough for our purposes
            DriscollException actual = assertThrows(DriscollException.class, () -> categoryService.deleteCategory(BAD_USER_ID, TEST.getCategoryName()));
            assertEquals(excepted.getStatus(), actual.getStatus());
            assertEquals(excepted.getMessage(), actual.getMessage());
        }

        @Test
        public void whenValidUserIdAndInvalidCategoryByName_throwDriscollException() throws DriscollException {
            DriscollException excepted = new DriscollException(CategoryExceptionEnums.CATEGORY_NOT_FOUND.getStatus(), CategoryExceptionEnums.CATEGORY_NOT_FOUND.getMessage());

            when(categoryRepository.findByUserIdAndCategoryName(USER_ID, TEST.getCategoryName())).thenReturn(null);

            DriscollException actual = assertThrows(DriscollException.class, () -> categoryService.deleteCategory(USER_ID, TEST.getCategoryName()));

            assertEquals(excepted.getStatus(), actual.getStatus());
            assertEquals(excepted.getMessage(), actual.getMessage());
        }
    }
}