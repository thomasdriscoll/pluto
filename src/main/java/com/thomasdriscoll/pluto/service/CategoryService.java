package com.thomasdriscoll.pluto.service;

import com.thomasdriscoll.pluto.lib.dao.CategoryDao;
import com.thomasdriscoll.pluto.lib.dao.CategoryRepository;
import com.thomasdriscoll.pluto.lib.exceptions.BudgetExceptionEnums;
import com.thomasdriscoll.pluto.lib.exceptions.CategoryExceptionEnums;
import com.thomasdriscoll.pluto.lib.exceptions.DriscollException;
import com.thomasdriscoll.pluto.lib.models.Category;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

    private CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository){
        this.categoryRepository = categoryRepository;
    }

    public Category createCategory(String userId, Category category) throws DriscollException {
        //Validate input
        validateUser(userId);
        validateCategory(userId, category);

        //Save input
        return categoryRepository.save(new CategoryDao(category)).toCategory();
    }

    public Category updateCategory(String userId, Category category) throws DriscollException {
        //Validate input
        return category;
    }

    public void deleteCategory(String userId, String categoryName) throws DriscollException {

    }


    // Helper functions
    // Need to update this validateUser with call to UserApi
    private void validateUser(String userId) throws DriscollException {
        if(categoryRepository.findByUserId(userId).isEmpty()){
            throw new DriscollException(CategoryExceptionEnums.INVALID_USER_ID.getStatus(), CategoryExceptionEnums.INVALID_USER_ID.getMessage());
        }
    }

    private void validateCategory(String userId, Category category) throws DriscollException {
        validateLogic(category.getIsNeed(), category.getIsWant(), category.getIsSavings(), category.getIsIncome());
        if(category.getParentCategory() != null){
            validateParent(userId, category.getParentCategory());
        }
        if(categoryRepository.findByUserIdAndCategoryName(userId, category.getCategoryName()) != null){
            throw new DriscollException(CategoryExceptionEnums.INVALID_CATEGORY_NAME.getStatus(), CategoryExceptionEnums.INVALID_CATEGORY_NAME.getMessage());
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
