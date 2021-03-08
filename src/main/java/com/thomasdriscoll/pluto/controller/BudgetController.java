package com.thomasdriscoll.pluto.controller;

import com.thomasdriscoll.pluto.lib.exceptions.DriscollException;
import com.thomasdriscoll.pluto.lib.responses.DriscollResponse;
import com.thomasdriscoll.pluto.service.BudgetService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class BudgetController {

    private BudgetService budgetService;

    public BudgetController(BudgetService budgetService){
        this.budgetService = budgetService;
    }

    @GetMapping("/{name}")
    private ResponseEntity<DriscollResponse<String>> dummyFunction(@PathVariable String name) throws DriscollException {
        return ResponseEntity.ok().body(new DriscollResponse<>(HttpStatus.OK.value(), budgetService.dummyFunction(name)));
    }
}

