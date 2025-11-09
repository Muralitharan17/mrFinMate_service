package com.murali.mrFinMate.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.murali.mrFinMate.dto.FinanceCategoryDTO;
import com.murali.mrFinMate.dto.FinanceDetailDTO;
import com.murali.mrFinMate.dto.FinanceRequest;
import com.murali.mrFinMate.dto.FinanceTypeDTO;
import com.murali.mrFinMate.entity.BudgetSection;
import com.murali.mrFinMate.entity.FinanceCategory;
import com.murali.mrFinMate.entity.FinanceDetail;
import com.murali.mrFinMate.entity.FinanceType;
import com.murali.mrFinMate.repository.service.BudgetSectionRepositorySevice;
import com.murali.mrFinMate.repository.service.FinanceCategoryRepositoryService;
import com.murali.mrFinMate.repository.service.FinanceDetailRepositoryService;
import com.murali.mrFinMate.repository.service.FinanceTypeRepositoryService;
import com.murali.mrFinMate.utils.PopulateUtils;

@Service
public class FinanceTypeControllerService {
	
	@Autowired
	FinanceTypeRepositoryService financeTypeRepositoryService;
	
	@Autowired
	BudgetSectionRepositorySevice budgetSectionRepositoryService;
	
	@Autowired
	FinanceDetailRepositoryService financeDetailRepositoryService;
	
	@Autowired
	FinanceCategoryRepositoryService financeCategoryRepositoryService;
	
	@Autowired
	PopulateUtils populateUtils;

	public List<FinanceTypeDTO> getFinanceTypeDetailsBySectionId(Long sectionId) {
		List<FinanceType> financeTypes = financeTypeRepositoryService.findByBudgetSectionId(sectionId);
		
		return financeTypes.stream().map(type -> {
			FinanceTypeDTO typeDTO = populateUtils.populateFinanceTypeDTOFromFinanceTypeEntity(type);
			
            List<FinanceCategoryDTO> categories = type.getCategories().stream().map(cat -> {
            	FinanceCategoryDTO catDTO = populateUtils.populateFinanceCategoryDTOFromFinanceCategoryEntity(cat);
            	
                List<FinanceDetailDTO> details = cat.getDetails().stream().map(det -> {
                	FinanceDetailDTO detDTO = populateUtils.populateFinanceDetailDTOFromFinanceDetailEntity(det);
                    return detDTO;
                }).collect(Collectors.toList());

                catDTO.setDetails(details);
                return catDTO;
            }).collect(Collectors.toList());

            typeDTO.setCategories(categories);
            return typeDTO;
        }).collect(Collectors.toList());
	}

	public void saveOrUpdateFinanceTypeConfig(FinanceRequest financeRequest) {
		try {
			
			BudgetSection section = budgetSectionRepositoryService.findById(financeRequest.getSectionId());
			
			// Fetch existing FinanceTypes for this config
		    List<FinanceType> existingTypes = financeTypeRepositoryService.findByBudgetSectionId(financeRequest.getSectionId());
		    Map<Long, FinanceType> existingTypeMap = existingTypes.stream()
		            .filter(t -> t.getId() != null)
		            .collect(Collectors.toMap(FinanceType::getId, t -> t));
			
			if(financeRequest != null && financeRequest.getFinanceTypes() != null && !financeRequest.getFinanceTypes().isEmpty()) {
				
				for (FinanceTypeDTO financeTypeDTO : financeRequest.getFinanceTypes()) {
					
					FinanceType financeType = existingTypeMap.getOrDefault(financeTypeDTO.getId(), new FinanceType());

	                if (financeType.getId() == null) {
	                	// Creating new one
	                    financeType.setBudgetConfig(section.getBudgetConfig());
	                    financeType.setBudgetSection(section);
	                }
					
					financeType.setTypeName(financeTypeDTO.getName());
					financeType.setTypePercentage(financeTypeDTO.getPercentage());
					financeType.setAllottedAmount(financeTypeDTO.getAllottedAmount());
					
					// Clear existing categories, reuse list instance
					if (financeType.getCategories() == null)
	                    financeType.setCategories(new ArrayList<>());
					 
					List<FinanceCategory> existingCategories = financeType.getCategories();
					 Map<Long, FinanceCategory> existingCategoryMap = existingCategories.stream()
				                .filter(c -> c.getId() != null)
				                .collect(Collectors.toMap(FinanceCategory::getId, c -> c));
					 
					// We'll rebuild the list in place
					 List<FinanceCategory> updatedCategories = new ArrayList<>();
					 
					 for (FinanceCategoryDTO financeCategoryDTO : financeTypeDTO.getCategories()) {
						 
						 FinanceCategory category = existingCategoryMap.getOrDefault(financeCategoryDTO.getId(), new FinanceCategory());
						 
						 if (category.getId() == null)
		                        category.setFinanceType(financeType);
						 
						 category.setCategoryName(financeCategoryDTO.getName());
				         category.setCategoryPercentage(financeCategoryDTO.getPercentage());
				         category.setAllottedAmount(financeCategoryDTO.getAllottedAmount());
				         
				         // manage details in place
				         if (category.getDetails() == null)
		                        category.setDetails(new ArrayList<>());
				         
				         List<FinanceDetail> existingDetails = category.getDetails();
				         Map<Long, FinanceDetail> existingDetailMap = existingDetails.stream()
				                    .filter(d -> d.getId() != null)
				                    .collect(Collectors.toMap(FinanceDetail::getId, d -> d));
				         System.out.println("existingDetailMap" + existingDetailMap);
				         List<FinanceDetail> updatedDetails = new ArrayList<>();
				         
				         for (FinanceDetailDTO financeDetailDTO : financeCategoryDTO.getDetails()) {
				        	 
				        	 FinanceDetail detail = existingDetailMap.getOrDefault(financeDetailDTO.getId(), new FinanceDetail());
				        	 
				        	 if (detail.getId() == null)
		                            detail.setFinanceCategory(category);
				        	 
				                System.out.println("Updating/Adding Detail ID: " + financeDetailDTO.getId() + " Name: " + financeDetailDTO.getName());
				                detail.setDetailName(financeDetailDTO.getName());
				                detail.setDetailPercentage(financeDetailDTO.getPercentage());
				                detail.setAllottedAmount(financeDetailDTO.getAllottedAmount());
				                updatedDetails.add(detail);
				            }
				         
				         existingDetails.removeIf(d -> d.getId() != null && updatedDetails.stream()
		                            .noneMatch(u -> u.getId() != null && u.getId().equals(d.getId())));
				         
				      // Handle deleted details
				         existingDetails.clear();
				         System.out.println("existingDetails: " + existingDetails.size());
				         
				         existingDetails.addAll(updatedDetails);
				         
				         updatedCategories.add(category);
				         
					 }
					 
					 existingCategories.removeIf(c -> c.getId() != null && updatedCategories.stream()
		                        .noneMatch(u -> u.getId() != null && u.getId().equals(c.getId())));
					 
					// Handle deleted categories
					 existingCategories.clear();
		             existingCategories.addAll(updatedCategories);

		             financeTypeRepositoryService.save(financeType);
		             
		             System.out.println("existingTypes: " + existingTypes.size() + " Name: " + financeType.getTypeName());
		             
		             // To remove the investment Type which are not in the request
		             existingTypes.stream()
	                    .filter(t -> t.getId() != null && financeRequest.getFinanceTypes().stream()
	                            .noneMatch(u -> t.getId().equals(u.getId())))
	                    .forEach(financeTypeRepositoryService::delete);
				}
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
