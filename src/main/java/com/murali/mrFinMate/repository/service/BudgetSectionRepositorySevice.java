package com.murali.mrFinMate.repository.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.murali.mrFinMate.dto.BudgetSectionDTO;
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

	public BudgetSection save(BudgetSection section) {
		return budgetSectionRepository.save(section);
		
	}

	public List<BudgetSectionDTO> sumAllSectionsForProfile(Long profileId) {
		return budgetSectionRepository.sumAllSectionsForProfile(profileId);
	}

	public List<BudgetSectionDTO> sumSectionsForYear(Long profileId, String year) {
		return budgetSectionRepository.sumSectionsForYear(profileId,year);
	}

	public List<BudgetSectionDTO> sumSectionsForMonthAcrossYears(Long profileId, String month) {
		return budgetSectionRepository.sumSectionsForMonthAcrossYears(profileId,month);
	}

}
