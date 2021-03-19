package com.thomasdriscoll.pluto.controller;

import com.thomasdriscoll.pluto.lib.exceptions.DriscollException;
import com.thomasdriscoll.pluto.lib.models.Category;
import com.thomasdriscoll.pluto.lib.responses.DriscollResponse;
import com.thomasdriscoll.pluto.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users/{userId}/category")
public class CategoryController {
    private CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<DriscollResponse<Category>> createCategory(
            @RequestBody Category category,
            @PathVariable String userId
    ) throws DriscollException {
        return ResponseEntity.ok(new DriscollResponse(HttpStatus.OK.value(), categoryService.createCategory(userId, category)));
    }
}
