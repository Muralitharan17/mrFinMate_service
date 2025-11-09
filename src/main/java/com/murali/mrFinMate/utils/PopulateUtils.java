package com.murali.mrFinMate.utils;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.murali.mrFinMate.dto.BudgetConfigDTO;
import com.murali.mrFinMate.dto.BudgetSectionDTO;
import com.murali.mrFinMate.dto.FinanceCategoryDTO;
import com.murali.mrFinMate.dto.FinanceDetailDTO;
import com.murali.mrFinMate.dto.FinanceTypeDTO;
import com.murali.mrFinMate.dto.ProfileDTO;
import com.murali.mrFinMate.entity.BudgetConfig;
import com.murali.mrFinMate.entity.BudgetSection;
import com.murali.mrFinMate.entity.FinanceCategory;
import com.murali.mrFinMate.entity.FinanceDetail;
import com.murali.mrFinMate.entity.FinanceType;
import com.murali.mrFinMate.entity.Profile;

@Service
public class PopulateUtils {
	
	@Autowired
	CommonUtils commonUtils;
	
	public BudgetSection populateBudgetSectionEntityFromBudgetSectionDTO(BudgetSectionDTO budgetSectionDTO) {
		BudgetSection budgetSection = null;
		try {
			if(budgetSectionDTO != null) {
				budgetSection = new BudgetSection();
				
				budgetSection.setSectionName(budgetSectionDTO.getName());
				budgetSection.setSectionPercentage(budgetSectionDTO.getPercentage());
				budgetSection.setAllottedAmount(budgetSectionDTO.getAllocatedAmount());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return budgetSection;
	}
	
	public BudgetSectionDTO populateBudgetSectionDTOFromBudgetSectionEntity(BudgetSection budgetSection) {
		BudgetSectionDTO budgetSectionDTO = null;
		try {
			if(budgetSection != null) {
				budgetSectionDTO = new BudgetSectionDTO();
				
				budgetSectionDTO.setId(budgetSection.getId());
				budgetSectionDTO.setName(budgetSection.getSectionName());
				budgetSectionDTO.setPercentage(budgetSection.getSectionPercentage());
				budgetSectionDTO.setAllocatedAmount(budgetSection.getAllottedAmount());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return budgetSectionDTO;
	}
	
	public ProfileDTO populateProfileDTOFromProfileEntity(Profile profile) {
		ProfileDTO profileDTO = null;
		try {
			if(profile != null) {
				List<BudgetConfigDTO> budgetConfigDTOList = null;
				profileDTO = new ProfileDTO();

				profileDTO.setId(profile.getId());
				profileDTO.setName(profile.getName());
				
				
				if (profile.getBudgetConfigs() != null && !profile.getBudgetConfigs().isEmpty()) {
					
					budgetConfigDTOList = profile.getBudgetConfigs().stream().map(entity -> {
						BudgetConfigDTO budgetConfigDTO = populateBudgetConfigDTOFromBudgetConfigEntity(entity);
						return budgetConfigDTO;
					}).toList();
					
					if(budgetConfigDTOList != null && !budgetConfigDTOList.isEmpty()) {
						profileDTO.setBudgetConfigs(budgetConfigDTOList);
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return profileDTO;
	}


	public BudgetConfigDTO populateBudgetConfigDTOFromBudgetConfigEntity(BudgetConfig budgetConfig) {
		BudgetConfigDTO budgetConfigDTO = null;
		try {
			if(budgetConfig != null) {
				budgetConfigDTO = new BudgetConfigDTO();
				List<BudgetSectionDTO> budgetSectionDTOList = null;

				budgetConfigDTO.setId(budgetConfig.getId());
				budgetConfigDTO.setActualSalary(budgetConfig.getActualSalary());
				budgetConfigDTO.setBudgetPercentage(budgetConfig.getBudgetPercentage());
				budgetConfigDTO.setBudgetSalary(budgetConfig.getBudgetSalary());
				
				if (budgetConfig.getSections() != null && !budgetConfig.getSections().isEmpty()) {
					budgetSectionDTOList = budgetConfig.getSections().stream().map(entity -> {
						BudgetSectionDTO budgetSectionDTO = populateBudgetSectionDTOFromBudgetSectionEntity(entity);
						return budgetSectionDTO;
					}).toList();
					
					if(budgetSectionDTOList != null && !budgetSectionDTOList.isEmpty()) {
						budgetConfigDTO.setSections(budgetSectionDTOList);
					}
				}
				
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return budgetConfigDTO;
	}

	public BudgetSection updateBudgetSectionEntityFromBudgetSectionDTO(BudgetSection budgetSection,
			BudgetSectionDTO budgetSectionDTO) {

		try {
			if(budgetSectionDTO != null) {
				
				if(budgetSection != null) {
					budgetSection = new BudgetSection();
				}
				
				budgetSection.setId(budgetSectionDTO.getId());
				budgetSection.setSectionName(budgetSectionDTO.getName());
				budgetSection.setSectionPercentage(budgetSectionDTO.getPercentage());
				budgetSection.setAllottedAmount(budgetSectionDTO.getAllocatedAmount());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return budgetSection;
	}

	public FinanceTypeDTO populateFinanceTypeDTOFromFinanceTypeEntity(FinanceType financeType) {
		FinanceTypeDTO financeTypeDTO = null;
		try {
			if(financeType != null) {
				financeTypeDTO = new FinanceTypeDTO();

				financeTypeDTO.setId(financeType.getId());
				financeTypeDTO.setSectionId(financeType.getBudgetSection().getId());
				financeTypeDTO.setName(financeType.getTypeName());
				financeTypeDTO.setPercentage(financeType.getTypePercentage());
				financeTypeDTO.setAllottedAmount(financeType.getAllottedAmount());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return financeTypeDTO;
	}

	public FinanceCategoryDTO populateFinanceCategoryDTOFromFinanceCategoryEntity(FinanceCategory cat) {
		FinanceCategoryDTO financeCategoryDTO = null;
		try {
			if (cat != null) {
				financeCategoryDTO = new FinanceCategoryDTO();

				financeCategoryDTO.setId(cat.getId());
				financeCategoryDTO.setName(cat.getCategoryName());
				financeCategoryDTO.setPercentage(cat.getCategoryPercentage());
				financeCategoryDTO.setAllottedAmount(cat.getAllottedAmount());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return financeCategoryDTO;
	}

	public FinanceDetailDTO populateFinanceDetailDTOFromFinanceDetailEntity(FinanceDetail det) {
		FinanceDetailDTO financeDetailDTO = null;
		try {
			if (det != null) {
				financeDetailDTO = new FinanceDetailDTO();

				financeDetailDTO.setId(det.getId());
				financeDetailDTO.setName(det.getDetailName());
				financeDetailDTO.setPercentage(det.getDetailPercentage());
				financeDetailDTO.setAllottedAmount(det.getAllottedAmount());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return financeDetailDTO;
	}
	
}
