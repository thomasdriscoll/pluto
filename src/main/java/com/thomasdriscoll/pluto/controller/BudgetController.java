package com.thomasdriscoll.pluto.controller;

import com.thomasdriscoll.pluto.lib.exceptions.DriscollException;
import com.thomasdriscoll.pluto.lib.models.Budget;
import com.thomasdriscoll.pluto.lib.models.BudgetRequest;
import com.thomasdriscoll.pluto.lib.responses.DriscollResponse;
import com.thomasdriscoll.pluto.service.BudgetService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users/{userId}/budget")
public class BudgetController {

    private final BudgetService budgetService;

    public BudgetController(BudgetService budgetService){
        this.budgetService = budgetService;
    }

    @PostMapping
    public ResponseEntity<DriscollResponse<Budget>> createBudget(
            @PathVariable String userId,
            @RequestBody BudgetRequest request
    ) throws DriscollException {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new DriscollResponse(
                        HttpStatus.CREATED.value(),
                        budgetService.createBudget(userId, request)
                )
        );
    }

    @GetMapping
    public ResponseEntity<DriscollResponse<Budget>> getBudget(
            @PathVariable String userId
    ) throws DriscollException {
        return ResponseEntity.ok(
                new DriscollResponse(
                        HttpStatus.OK.value(),
                        budgetService.getBudget(userId)
                )
        );
    }
}

