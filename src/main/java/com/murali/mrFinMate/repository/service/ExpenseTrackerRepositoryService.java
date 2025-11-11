package com.murali.mrFinMate.repository.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.murali.mrFinMate.entity.ExpenseTracker;
import com.murali.mrFinMate.repository.ExpenseTrackerRepository;

@Service
public class ExpenseTrackerRepositoryService {
	
	@Autowired
	private ExpenseTrackerRepository expenseTrackerRepository;

	public ExpenseTracker save(ExpenseTracker expense) {
		return expenseTrackerRepository.save(expense);
	}

	public Optional<ExpenseTracker> findById(Long id) {
		return expenseTrackerRepository.findById(id);
	}

	public void deleteById(Long id) {
		expenseTrackerRepository.deleteById(id);
		
	}

	public List<ExpenseTracker> findByProfileIdAndMonthAndYear(Long profileId, String month, String year) {
		return expenseTrackerRepository.findByProfileIdAndMonthAndYear(profileId, month, year);
	}

}
