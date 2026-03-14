package com.murali.mrFinMate.repository.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.murali.mrFinMate.dto.BudgetConfigDTO;
import com.murali.mrFinMate.dto.BudgetSectionDTO;
import com.murali.mrFinMate.entity.BudgetConfig;
import com.murali.mrFinMate.entity.BudgetSection;
import com.murali.mrFinMate.entity.Profile;
import com.murali.mrFinMate.repository.BudgetConfigRepository;
import com.murali.mrFinMate.repository.ProfileRepository;
import com.murali.mrFinMate.utils.PopulateUtils;

import jakarta.transaction.Transactional;

@Service
public class BudgetConfigRepositoryService {
	
	@Autowired
	BudgetConfigRepository budgetConfigRepository;
	
	@Autowired
	ProfileRepository profileRepository;
	
	@Autowired
	PopulateUtils populateUtils;

	public BudgetConfig findByProfileIdAndMonthAndYear(Long profileId, String month, String year) {
		Optional<BudgetConfig> optional = budgetConfigRepository.findByProfile_IdAndMonthAndYear(profileId, month, year);
		return optional.orElse(null);
	}
	
	@Transactional
    public BudgetConfig saveOrUpdateBudgetConfig(BudgetConfigDTO budgetConfigDTO) {
		
        // Fetch the profile
        Profile profile = profileRepository.findById(budgetConfigDTO.getProfileId())
                .orElseThrow(() -> new IllegalArgumentException("Profile not found"));

        // Check if existing config is present
        Optional<BudgetConfig> existingConfigOpt = budgetConfigRepository
                .findByProfile_IdAndMonthAndYear(budgetConfigDTO.getProfileId(), budgetConfigDTO.getMonth(), budgetConfigDTO.getYear());

        BudgetConfig config;
        if (existingConfigOpt.isPresent()) {
            config = existingConfigOpt.get();
        } else {
            config = new BudgetConfig();
            config.setProfile(profile);
            config.setMonth(budgetConfigDTO.getMonth());
            config.setYear(budgetConfigDTO.getYear());
        }

        // Update salary and sections
        config.setActualSalary(budgetConfigDTO.getActualSalary());
        config.setBudgetPercentage(budgetConfigDTO.getBudgetPercentage());
        config.setBudgetSalary(budgetConfigDTO.getBudgetSalary());
        config.setCreatedUser(budgetConfigDTO.getCreatedUser());
        config.setUpdatedUser(budgetConfigDTO.getUpdatedUser());
        config.setCreatedDate(budgetConfigDTO.getCreatedDate());
        config.setUpdatedDate(budgetConfigDTO.getUpdatedDate());
        
     // Handle sections
        List<BudgetSection> existingSections = (config.getSections() != null && !config.getSections().isEmpty())
                ? new ArrayList<>(config.getSections())
                : new ArrayList<>();
        
        Map<Long, BudgetSection> existingSectionMap = existingSections.stream()
                .filter(s -> s.getId() != null)
                .collect(Collectors.toMap(BudgetSection::getId, s -> s));
        
        List<BudgetSection> updatedSections = new ArrayList<>();
        
        if (budgetConfigDTO.getSections() != null && !budgetConfigDTO.getSections().isEmpty()) {
        	for (BudgetSectionDTO budgetSectionDTO : budgetConfigDTO.getSections()) {
        		BudgetSection budgetSection;
        		
        		if (budgetSectionDTO.getId() != null && existingSectionMap.containsKey(budgetSectionDTO.getId())) {
        			// Update existing section
        			budgetSection = existingSectionMap.get(budgetSectionDTO.getId());
        			budgetSection = populateUtils.updateBudgetSectionEntityFromBudgetSectionDTO(budgetSection, budgetSectionDTO);
        		} else {
					// Create new section
					budgetSection = populateUtils.populateBudgetSectionEntityFromBudgetSectionDTO(budgetSectionDTO);
        		}
        		
        		budgetSection.setBudgetConfig(config);
        		 
        		updatedSections.add(budgetSection);
        	}
        }
        
     // Remove sections not in the DTO (deleted ones)
        existingSections.stream()
                .filter(section -> section.getId() != null && updatedSections.stream()
                        .noneMatch(u -> section.getId().equals(u.getId())));

		if (config.getSections() == null) {
			config.setSections(new ArrayList<>());
		}
        config.getSections().clear();
        config.getSections().addAll(updatedSections);
        

        return budgetConfigRepository.save(config);
    }

	public BudgetConfig save(BudgetConfig budgetConfig) {
		return budgetConfigRepository.save(budgetConfig);
	}

	public BudgetConfig findById(Long id) {
		Optional<BudgetConfig> optional = budgetConfigRepository.findById(id);
		return optional.orElse(null);
	}

	public BudgetConfigDTO fetchAllBudgetForProfile(Long profileId) {
		BudgetConfigDTO BudgetConfig =  budgetConfigRepository.sumAllBudgetForProfile(profileId);
		return BudgetConfig;
	}

	public BudgetConfigDTO fetchBudgetForYear(Long profileId, String year) {
		return budgetConfigRepository.sumBudgetForYear(profileId, year);
	}

	public BudgetConfigDTO fetchBudgetForMonthAcrossYears(Long profileId, String month) {
		return budgetConfigRepository.sumBudgetForMonthAcrossYears(profileId, month);
	}

}
