package com.murali.mrFinMate.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.murali.mrFinMate.constants.CommonConstants;
import com.murali.mrFinMate.dto.MasterConfigDTO;
import com.murali.mrFinMate.entity.BudgetConfig;
import com.murali.mrFinMate.entity.BudgetSection;
import com.murali.mrFinMate.entity.FinanceCategory;
import com.murali.mrFinMate.entity.FinanceDetail;
import com.murali.mrFinMate.entity.FinanceType;
import com.murali.mrFinMate.entity.MasterConfig;
import com.murali.mrFinMate.entity.Profile;
import com.murali.mrFinMate.repository.ProfileRepository;
import com.murali.mrFinMate.repository.service.BudgetConfigRepositoryService;
import com.murali.mrFinMate.repository.service.BudgetSectionRepositorySevice;
import com.murali.mrFinMate.repository.service.FinanceTypeRepositoryService;
import com.murali.mrFinMate.repository.service.MasterConfigRepositoryService;
import com.murali.mrFinMate.utils.CommonUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MasterConfigControllerService {

    private final MasterConfigRepositoryService masterConfigRepositoryService;
    private final ProfileRepository profileRepository;
    
    @Autowired
    BudgetConfigRepositoryService budgetConfigRepositoryService;
    
    @Autowired
    BudgetSectionRepositorySevice budgetSectionRepositoryService;
    
    @Autowired
    FinanceTypeRepositoryService financeTypeRepositoryService;
    
    @Autowired
    CommonUtils commonUtils;

    public List<MasterConfigDTO> fetchMasterConfig(Long profileId) {
        return masterConfigRepositoryService.getConfigsByProfile(profileId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public MasterConfigDTO saveOrUpdateMasterConfig(MasterConfigDTO dto) {
        Profile profile = profileRepository.findById(dto.getProfileId())
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        // Check for duplicate
        List<MasterConfig> existingConfigs =
                masterConfigRepositoryService.findByProfileIdAndConfigName(dto.getProfileId(), dto.getConfigName());

        boolean duplicateExists = existingConfigs.stream().anyMatch(existing -> {
            boolean bothMonthYearNull = existing.getMonth() == null && existing.getYear() == null;
            boolean newMonthYearNull = dto.getMonth() == null && dto.getYear() == null;

            if (bothMonthYearNull && newMonthYearNull) {
                return true; // duplicate global config
            }

            return (existing.getMonth() != null && existing.getYear() != null
                    && existing.getMonth().equals(dto.getMonth())
                    && existing.getYear().equals(dto.getYear()));
        });

        if (duplicateExists && (dto.getId() == null)) {
            throw new RuntimeException("Duplicate configuration exists for given month/year or global.");
        }

        MasterConfig config = (dto.getId() != null)
                ? masterConfigRepositoryService.findExisting(dto.getProfileId(), dto.getConfigName(), dto.getMonth(), dto.getYear())
                        .orElse(new MasterConfig())
                : new MasterConfig();

        config.setProfile(profile);
        config.setConfigName(dto.getConfigName());
        config.setConfigValue(dto.getConfigValue());
        config.setDescription(dto.getDescription());
        config.setMonth(dto.getMonth());
        config.setYear(dto.getYear());
        config.setCreatedUser(dto.getCreatedUser());
        config.setUpdatedUser(dto.getUpdatedUser());
        config.setActive(dto.getActive() != null ? dto.getActive() : true);

        return convertToDTO(masterConfigRepositoryService.save(config));
    }

    public void deleteMasterConfig(Long configId) {
        masterConfigRepositoryService.delete(configId);
    }

    private MasterConfigDTO convertToDTO(MasterConfig config) {
        return MasterConfigDTO.builder()
                .id(config.getId())
                .profileId(config.getProfile().getId())
                .profileName(config.getProfile().getName())
                .configName(config.getConfigName())
                .configValue(config.getConfigValue())
                .description(config.getDescription())
                .month(config.getMonth())
                .year(config.getYear())
                .active(config.getActive())
                .createdUser(config.getCreatedUser())
                .updatedUser(config.getUpdatedUser())
                .createdDate(config.getCreatedDate())
                .updatedDate(config.getUpdatedDate())
                .build();
    }
    
    public String getConfigValue(Long profileId, String month, String year, String configName) {
    	
    	
		if (commonUtils.isNotNull(configName)) {
			configName = configName.replaceAll("-", " ").toUpperCase();
		}
    	
    	
    	// Fetch using Section Type
    	String configValue = fetchConfigValueUsingSectionType(profileId, month, year, configName);
    	System.out.println("Config Value from Section Type: " + configValue);
    	
    	if(configValue == null) {
    		Optional<MasterConfig> configOpt = masterConfigRepositoryService
                    .findExisting(profileId, configName, month, year);

            if (configOpt.isPresent()) {
            	configValue = configOpt.get().getConfigValue();
            }
            
            List<MasterConfig> globalOptList = masterConfigRepositoryService
                    .findByProfileIdAndConfigName(profileId, configName);
            
    		if (globalOptList != null && !globalOptList.isEmpty()) {
    			configValue = globalOptList.get(0).getConfigValue();
    		}
    	}
    	
        

        return configValue;
    }

	private String fetchConfigValueUsingSectionType(Long profileId, String month, String year, String configName) {
		String financeTypes = "";
		String categoryTypes = "";
		String detailTypes = "";

		try {

			if (profileId > 0 && commonUtils.isNotNull(configName) && configName.split("_").length > 2
					&& commonUtils.isNotNull(month) && commonUtils.isNotNull(year)) {

				String configNamePrefix = configName.split("_")[0] + "_" + configName.split("_")[1];
				String sectionName = configName.split("_")[2];
				
				String financeTypeName = "";
				String categoryName = "";
				
				if(configName.split("_").length > 3) {
					financeTypeName = configName.split("_")[3];
				}
				
				if(configName.split("_").length > 4) {
					categoryName = configName.split("_")[4];
				}
				
				System.out.println("financeTypeName :" + financeTypeName);
				System.out.println("categoryName :" + categoryName);
				
				

				BudgetConfig budgetConfig = budgetConfigRepositoryService.findByProfileIdAndMonthAndYear(profileId,
						month, year);

				if (budgetConfig != null) {
					BudgetSection budgetSection = budgetSectionRepositoryService
							.findByBudgetConfigIdAndSectionName(budgetConfig.getId(), sectionName);

					List<FinanceType> financeTypeList = financeTypeRepositoryService
							.findByBudgetSectionId(budgetSection.getId());
					for (FinanceType financetype : financeTypeList) {
						
						if (commonUtils.isNotNull(financeTypeName) && !financeTypeName.isEmpty()
								&& !financetype.getTypeName().equalsIgnoreCase(financeTypeName)) {
							continue;
						}

						if (commonUtils.isNull(financeTypes)) {
							financeTypes = financetype.getTypeName();
						} else {
							financeTypes = financeTypes + "," + financetype.getTypeName();
						}

						// To break the below process, since it is not needed
						if (configNamePrefix.equalsIgnoreCase(CommonConstants.FINANCE_TYPES)) {
							continue;
						}

						// Fetching Category Types
						for (FinanceCategory financeCategory : financetype.getCategories()) {
							
							if (commonUtils.isNotNull(categoryName) && !categoryName.isEmpty()
									&& !financeCategory.getCategoryName().equalsIgnoreCase(categoryName)) {
								continue;
							}
							
							if (commonUtils.isNull(categoryTypes)) {
								categoryTypes = financeCategory.getCategoryName();
							} else {
								categoryTypes = categoryTypes + "," + financeCategory.getCategoryName();
							}

							// To break the below process, since it is not needed
							if (configNamePrefix.equalsIgnoreCase(CommonConstants.CATEGORY_TYPES)) {
								continue;
							}

							for (FinanceDetail financeDetail : financeCategory.getDetails()) {
								if (commonUtils.isNull(detailTypes)) {
									detailTypes = financeDetail.getDetailName();
								} else {
									detailTypes = detailTypes + "," + financeDetail.getDetailName();
								}
							}
						}

					}
				}

				if (configNamePrefix.equalsIgnoreCase(CommonConstants.FINANCE_TYPES)
						&& commonUtils.isNotNull(financeTypes)) {
					return financeTypes;
				} else if (configNamePrefix.equalsIgnoreCase(CommonConstants.CATEGORY_TYPES)
						&& commonUtils.isNotNull(categoryTypes)) {
					return categoryTypes;
				} else if (commonUtils.isNotNull(detailTypes)) {
					return detailTypes;
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
}
