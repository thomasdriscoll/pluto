package com.thomasdriscoll.pluto.service;

import com.thomasdriscoll.pluto.lib.dao.CategoryDao;
import com.thomasdriscoll.pluto.lib.dao.CategoryRepository;
import com.thomasdriscoll.pluto.lib.exceptions.CategoryExceptionEnums;
import com.thomasdriscoll.pluto.lib.exceptions.DriscollException;
import com.thomasdriscoll.pluto.lib.models.Category;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {
    // Global vars
    private CategoryRepository categoryRepository;

    // Constructor
    public CategoryService(CategoryRepository categoryRepository){
        this.categoryRepository = categoryRepository;
    }

    //Main functionality
    public Category createCategory(String userId, Category category) throws DriscollException {
        //Validate input
        validateUser(userId);
        validateCategoryOnCreate(userId, category);

        //Save input
        return categoryRepository.save(new CategoryDao(userId, category)).toCategory();
    }

    public Category updateCategory(String userId, Category category) throws DriscollException {
        //Validate input
        validateUser(userId);

        //Update the category
        CategoryDao dao = validateCategoryOnUpdate(userId, category);
        dao.updateCategory(category);

        return categoryRepository.save(dao).toCategory();
    }

    public void deleteCategory(String userId, String categoryName) throws DriscollException {
        //Validate input
        validateUser(userId);
        validateCategoryOnDelete(userId, categoryName);
        categoryRepository.deleteByUserIdAndCategoryName(userId, categoryName);
    }


    // Helper functions
    // Need to update this validateUser with call to UserApi
    private void validateUser(String userId) throws DriscollException {
        if(categoryRepository.findByUserId(userId).isEmpty()){
            throw new DriscollException(CategoryExceptionEnums.INVALID_USER_ID.getStatus(), CategoryExceptionEnums.INVALID_USER_ID.getMessage());
        }
    }

    private void validateCategoryOnCreate(String userId, Category category) throws DriscollException {
        validateLogic(category.getIsNeed(), category.getIsWant(), category.getIsSavings(), category.getIsIncome());
        if(category.getParentCategory() != null){
            validateParent(userId, category.getParentCategory());
        }
        if(categoryRepository.findByUserIdAndCategoryName(userId, category.getCategoryName()) != null){
            throw new DriscollException(CategoryExceptionEnums.INVALID_CATEGORY_NAME.getStatus(), CategoryExceptionEnums.INVALID_CATEGORY_NAME.getMessage());
        }
    }

    private CategoryDao validateCategoryOnUpdate(String userId, Category category) throws DriscollException {
        validateLogic(category.getIsNeed(), category.getIsWant(), category.getIsSavings(), category.getIsIncome());
        if(category.getParentCategory() != null){
            validateParent(userId, category.getParentCategory());
        }
        CategoryDao dao = categoryRepository.findByUserIdAndCategoryName(userId, category.getCategoryName());
        if(dao == null){
            throw new DriscollException(CategoryExceptionEnums.CATEGORY_NOT_FOUND.getStatus(), CategoryExceptionEnums.CATEGORY_NOT_FOUND.getMessage());
        }
        return dao;
    }

    private void validateCategoryOnDelete(String userId, String categoryName) throws DriscollException {
        if(categoryRepository.findByUserIdAndCategoryName(userId, categoryName) == null){
            throw new DriscollException(CategoryExceptionEnums.CATEGORY_NOT_FOUND.getStatus(), CategoryExceptionEnums.CATEGORY_NOT_FOUND.getMessage());
        }
    }

    private void validateLogic(Boolean a, Boolean b, Boolean c, Boolean d) throws DriscollException {
        if(!((!a && !b && !c && d) || (!a && !b && c && !d) || (!a && b && !c && !d) || (a && !b && !c && !d))){
            throw new DriscollException(CategoryExceptionEnums.INVALID_CATEGORY_STATE.getStatus(), CategoryExceptionEnums.INVALID_CATEGORY_STATE.getMessage());
        }
    }

    private void validateParent(String userId, String parent) throws DriscollException {
        if(categoryRepository.findByUserIdAndParentCategory(userId, parent).isEmpty()){
            throw new DriscollException(CategoryExceptionEnums.INVALID_CATEGORY_PARENT.getStatus(), CategoryExceptionEnums.INVALID_CATEGORY_PARENT.getMessage());
        }
    }

}
