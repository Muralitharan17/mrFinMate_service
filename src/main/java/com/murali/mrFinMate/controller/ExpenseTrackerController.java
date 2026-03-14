package com.murali.mrFinMate.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.murali.mrFinMate.dto.ExpenseFilterRequestDTO;
import com.murali.mrFinMate.entity.ExpenseTracker;
import com.murali.mrFinMate.service.ExpenseTrackerControllerService;

@RestController
@CrossOrigin(origins = "*") // allow React front-end
public class ExpenseTrackerController {

	@Autowired
    ExpenseTrackerControllerService expenseTrackerControllerService;

    @GetMapping("/expenses")
    public ResponseEntity<List<ExpenseTracker>> getExpenses(
            @RequestParam Long profileId,
            @RequestParam String month,
            @RequestParam String year,
            @RequestParam(required = false) boolean onlyExpenses
            ) {
        return ResponseEntity.ok(expenseTrackerControllerService.getExpenses(profileId, month, year,onlyExpenses));
    }

    @PostMapping("/expenses")
    public ResponseEntity<ExpenseTracker> saveOrUpdateExpense(@RequestBody ExpenseTracker expense) {
    	ExpenseTracker saved = expenseTrackerControllerService.saveOrUpdateExpense(expense);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/expenses/{id}")
    public ResponseEntity<ExpenseTracker> updateExpense(@PathVariable Long id, @RequestBody ExpenseTracker expense) {
    	ExpenseTracker saved = expenseTrackerControllerService.updateExpense(id, expense);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/expenses/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable Long id) {
    	expenseTrackerControllerService.deleteExpense(id);
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping("expenses/filter")
	public ResponseEntity<List<ExpenseTracker>> filterExpenses(@RequestBody ExpenseFilterRequestDTO expenseFilterRequestDTO) {
		List<ExpenseTracker> filteredExpenses = expenseTrackerControllerService.filterExpenses(expenseFilterRequestDTO);
		return ResponseEntity.ok(filteredExpenses);
	}
}
