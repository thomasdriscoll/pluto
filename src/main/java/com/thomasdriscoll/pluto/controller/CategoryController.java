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
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<DriscollResponse<Category>> createCategory(
            @RequestBody Category category,
            @PathVariable String userId
    ) throws DriscollException {
        return ResponseEntity.status(HttpStatus.CREATED).body(new DriscollResponse<>(HttpStatus.CREATED.value(), categoryService.createCategory(userId, category)));
    }

    @PutMapping
    public ResponseEntity<DriscollResponse<Category>> updateCategory(
            @RequestBody Category category,
            @PathVariable String userId
    ) throws DriscollException {
        return ResponseEntity.ok(new DriscollResponse<>(HttpStatus.OK.value(), categoryService.updateCategory(userId, category)));
    }

    @DeleteMapping
    @RequestMapping("/{categoryName}")
    public ResponseEntity<DriscollResponse> deleteCategory (
        @PathVariable String userId,
        @PathVariable String categoryName
    ) throws DriscollException {
        categoryService.deleteCategory(userId, categoryName);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new DriscollResponse(HttpStatus.OK.value(), null));

    }
}
