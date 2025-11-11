package com.murali.mrFinMate.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.murali.mrFinMate.dto.BudgetConfigDTO;
import com.murali.mrFinMate.dto.BudgetSectionDTO;
import com.murali.mrFinMate.service.BudgetConfigControllerService;

@RestController
@CrossOrigin(origins = "*") // allow React front-end
public class BudgetConfigController {
	
	private final BudgetConfigControllerService budgetConfigControllerService;

    public BudgetConfigController(BudgetConfigControllerService budgetConfigControllerService) {
        this.budgetConfigControllerService = budgetConfigControllerService;
    }

    @GetMapping("/fetchBudgetConfig")
    public BudgetConfigDTO getBudgetConfig(
            @RequestParam Long profileId,
            @RequestParam String month,
            @RequestParam String year) {

        return budgetConfigControllerService.fetchBudgetConfig(profileId, month, year);
    }
    
    @PostMapping("/saveBudgetConfig")
    public BudgetConfigDTO saveBudgetConfig(@RequestBody BudgetConfigDTO budgetConfigDTO) {
        return budgetConfigControllerService.saveOrUpdateBudgetConfig(budgetConfigDTO);
    }
    
    @GetMapping("/fetchBudgetSections")
    public List<BudgetSectionDTO> getBudgetSections(
            @RequestParam Long profileId,
            @RequestParam String month,
            @RequestParam String year) {

        return budgetConfigControllerService.fetchBudgetConfig(profileId, month, year).getSections();
    }

}

