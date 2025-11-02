package com.murali.mrFinMate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.murali.mrFinMate.dto.BudgetConfigDTO;
import com.murali.mrFinMate.repository.service.BudgetConfigRepositoryService;
import com.murali.mrFinMate.repository.service.ProfileRepositoryService;
import com.murali.mrFinMate.utils.PopulateUtils;

@Service
public class BudgetConfigControllerService {
	
	@Autowired
	BudgetConfigRepositoryService budgetConfigRepositoryService;
	
	@Autowired
	ProfileRepositoryService profileRepositoryService;
	
	@Autowired
	PopulateUtils populateUtils;

	public BudgetConfigDTO fetchBudgetConfig(Long profileId, String month, String year) {
		return populateUtils.populateBudgetConfigDTOFromBudgetConfigEntity(budgetConfigRepositoryService.findByProfileIdAndMonthAndYear(profileId, month, year));
	}

	public BudgetConfigDTO saveOrUpdateBudgetConfig(BudgetConfigDTO budgetConfigDTO) {
		return populateUtils.populateBudgetConfigDTOFromBudgetConfigEntity(budgetConfigRepositoryService.saveOrUpdateBudgetConfig(budgetConfigDTO));
	}

}
