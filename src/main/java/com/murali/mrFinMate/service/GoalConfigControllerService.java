package com.murali.mrFinMate.service;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.murali.mrFinMate.constants.CommonConstants;
import com.murali.mrFinMate.entity.ExpenseTracker;
import com.murali.mrFinMate.entity.GoalConfig;
import com.murali.mrFinMate.repository.service.GoalConfigRepositoryService;
import com.murali.mrFinMate.repository.service.ProfileRepositoryService;
import com.murali.mrFinMate.utils.PopulateUtils;

@Service
public class GoalConfigControllerService {
	
	@Autowired
	ProfileRepositoryService profileRepositoryService;
	
	@Autowired
	GoalConfigRepositoryService GoalConfigRepositoryService;
	
	@Autowired
	PopulateUtils populateUtils;

	public List<GoalConfig> getGoals(Long profileId) {
		return GoalConfigRepositoryService.findByProfileIdAndIsActive(profileId);
	}

	public GoalConfig saveGoal(GoalConfig goalConfig) {
		return GoalConfigRepositoryService.save(goalConfig);
	}

	public void deleteGoal(Integer id) {
		GoalConfigRepositoryService.deleteById(id);
	}

	public void updateGoalIfMatched(ExpenseTracker expenseTracker, BigDecimal amount, boolean isDeleteRequest) {
		if(expenseTracker != null) {
			if(expenseTracker.getTransactionType().equals(CommonConstants.DEBIT)) {
				Set<String> names = new HashSet<String>();
				names.add(expenseTracker.getSectionType());
				names.add(expenseTracker.getFinanceType());
				names.add(expenseTracker.getFinanceCategory());
				names.add(expenseTracker.getFinanceDetail());
				
				if(expenseTracker.getSectionType().equals(CommonConstants.GOALS) || isDeleteRequest) {
					updateCurrentAmount(expenseTracker.getProfileId(), names, amount, false);
				} else {
					updateCurrentAmount(expenseTracker.getProfileId(), names, amount, true);
				}
			}
		}
	}
	
	public void updateCurrentAmount(Long profileId, Set<String> names, BigDecimal amount, boolean toAdd) {
		System.out.println("Updating goal amounts for profileId: " + profileId + ", names: " + names + ", amount: " + amount + ", toAdd: " + toAdd);
		for(String name : names) {
			GoalConfig config = GoalConfigRepositoryService.findByProfileIdAndGoalName(profileId, name);

	        if (config != null) {
	        	
	        	if(toAdd) {
	        		config.setCurrentAmount(config.getCurrentAmount().add(amount));
	        	} else {
	        		config.setCurrentAmount(config.getCurrentAmount().subtract(amount));
	        	}
	            
	            GoalConfigRepositoryService.save(config);
	        }
		}
        
    }

}
