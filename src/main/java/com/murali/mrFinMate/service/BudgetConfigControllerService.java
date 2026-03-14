package com.murali.mrFinMate.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.murali.mrFinMate.constants.CommonConstants;
import com.murali.mrFinMate.dto.BudgetConfigDTO;
import com.murali.mrFinMate.dto.BudgetSectionDTO;
import com.murali.mrFinMate.repository.service.BudgetConfigRepositoryService;
import com.murali.mrFinMate.repository.service.BudgetSectionRepositorySevice;
import com.murali.mrFinMate.repository.service.ProfileRepositoryService;
import com.murali.mrFinMate.utils.PopulateUtils;

@Service
public class BudgetConfigControllerService {
	
	@Autowired
	BudgetConfigRepositoryService budgetConfigRepositoryService;
	
	@Autowired
	BudgetSectionRepositorySevice budgetSectionRepositorySevice;
	
	@Autowired
	ProfileRepositoryService profileRepositoryService;
	
	@Autowired
	PopulateUtils populateUtils;

	public BudgetConfigDTO fetchBudgetConfig(Long profileId, String month, String year) {
		
		System.out.println("Fetching budget config for profileId: " + profileId + ", month: " + month + ", year: " + year);
		// ALL case — no month filter & no year filter
	    if (month.equals(CommonConstants.ALL) && year.equals(CommonConstants.ALL)) {
	    	System.out.println("Fetching all budget configs for profileId: " + profileId);
	    	
	    	BudgetConfigDTO BudgetConfigDTO = budgetConfigRepositoryService.fetchAllBudgetForProfile(profileId);
	    	List<BudgetSectionDTO> budgetSectionDTOList = budgetSectionRepositorySevice.sumAllSectionsForProfile(profileId);
	    	BudgetConfigDTO.setSections(budgetSectionDTOList);  
	    	
	    	return BudgetConfigDTO;
	    }

	    // ALL MONTHS of a year
	    if (month.equals(CommonConstants.ALL)) {
	    	BudgetConfigDTO BudgetConfigDTO = budgetConfigRepositoryService.fetchBudgetForYear(profileId, year);
	    	List<BudgetSectionDTO> budgetSectionDTOList = budgetSectionRepositorySevice.sumSectionsForYear(profileId, year);
	    	BudgetConfigDTO.setSections(budgetSectionDTOList);  
	    	return BudgetConfigDTO;
	    }

	    // ALL YEARS for a month
	    if (year.equals(CommonConstants.ALL)) {
	        
	    	BudgetConfigDTO BudgetConfigDTO = budgetConfigRepositoryService.fetchBudgetForMonthAcrossYears(profileId, month);
	    	List<BudgetSectionDTO> budgetSectionDTOList = budgetSectionRepositorySevice.sumSectionsForMonthAcrossYears(profileId, month);
	    	BudgetConfigDTO.setSections(budgetSectionDTOList);  
	    	return BudgetConfigDTO;
	    }
	    
		return populateUtils.populateBudgetConfigDTOFromBudgetConfigEntity(budgetConfigRepositoryService.findByProfileIdAndMonthAndYear(profileId, month, year));
	}

	public BudgetConfigDTO saveOrUpdateBudgetConfig(BudgetConfigDTO budgetConfigDTO) {
		return populateUtils.populateBudgetConfigDTOFromBudgetConfigEntity(budgetConfigRepositoryService.saveOrUpdateBudgetConfig(budgetConfigDTO));
	}

}
