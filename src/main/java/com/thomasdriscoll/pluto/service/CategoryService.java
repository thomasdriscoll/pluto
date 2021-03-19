package com.thomasdriscoll.pluto.service;

import com.thomasdriscoll.pluto.lib.exceptions.DriscollException;
import com.thomasdriscoll.pluto.lib.models.Category;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

    public Category createCategory(String userId, Category category) throws DriscollException {
        //Validate input

        return category;
    }
}
