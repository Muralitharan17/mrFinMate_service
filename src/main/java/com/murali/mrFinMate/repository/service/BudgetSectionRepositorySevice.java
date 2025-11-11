package com.murali.mrFinMate.repository.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.murali.mrFinMate.entity.BudgetSection;
import com.murali.mrFinMate.repository.BudgetSectionRepository;

@Service
public class BudgetSectionRepositorySevice {
	
	@Autowired
	BudgetSectionRepository budgetSectionRepository;

	public BudgetSection findById(Long sectionId) {
		return budgetSectionRepository.findById(sectionId)
                .orElseThrow(() -> new RuntimeException("Section not found"));
	}
	
	public BudgetSection findByBudgetConfigIdAndSectionName(Long budgetConfigId, String sectionName) {
		return budgetSectionRepository.findByBudgetConfig_IdAndSectionNameIgnoreCase(budgetConfigId, sectionName)
                .orElseThrow(() -> new RuntimeException("Section not found"));
	}

	public void save(BudgetSection section) {
		budgetSectionRepository.save(section);
		
	}

}
