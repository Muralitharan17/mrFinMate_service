package com.murali.mrFinMate.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.murali.mrFinMate.dto.BudgetConfigDTO;
import com.murali.mrFinMate.entity.BudgetConfig;
import com.murali.mrFinMate.entity.BudgetSection;
import com.murali.mrFinMate.entity.FinanceCategory;
import com.murali.mrFinMate.entity.FinanceDetail;
import com.murali.mrFinMate.entity.FinanceType;
import com.murali.mrFinMate.entity.MasterConfig;
import com.murali.mrFinMate.repository.service.BudgetConfigRepositoryService;
import com.murali.mrFinMate.repository.service.BudgetSectionRepositorySevice;
import com.murali.mrFinMate.repository.service.FinanceCategoryRepositoryService;
import com.murali.mrFinMate.repository.service.FinanceDetailRepositoryService;
import com.murali.mrFinMate.repository.service.FinanceTypeRepositoryService;
import com.murali.mrFinMate.repository.service.MasterConfigRepositoryService;
import com.murali.mrFinMate.utils.PopulateUtils;

import jakarta.transaction.Transactional;

@Service
public class InheritanceControllerService {
	
	@Autowired
	BudgetConfigRepositoryService budgetConfigRepositoryService;
	
	@Autowired
	BudgetSectionRepositorySevice budgetSectionRepositorySevice;
	
	@Autowired
	FinanceTypeRepositoryService financeTypeRepositoryService;
	
	@Autowired
	FinanceCategoryRepositoryService financeCategoryRepositoryService;
	
	@Autowired
	FinanceDetailRepositoryService financeDetailRepositoryService;
	
	@Autowired
	MasterConfigRepositoryService masterConfigRepositoryService;
	
	@Autowired
	PopulateUtils populateUtils;

	/**
     * Perform deep clone of config entities:
     * - budget_config (root)
     * - budget_section -> finance_type -> finance_category -> finance_detail
     * - master_config rows for the source that match month/year or are month/year-specific as requested
     *
     * All clones are saved with new DB-generated IDs.
     */
    @Transactional
    public BudgetConfigDTO inherit(Long profileId, String sourceMonth, String sourceYear, String targetMonth, String targetYear) {
        if (profileId == null) {
            throw new IllegalArgumentException("profileId is required");
        }
        if (sourceMonth == null || sourceYear == null) {
            throw new IllegalArgumentException("source month/year required");
        }
        if (targetMonth == null || targetYear == null) {
            throw new IllegalArgumentException("target month/year required");
        }

        // check source budget config exists
        BudgetConfig source = budgetConfigRepositoryService.findByProfileIdAndMonthAndYear(profileId, sourceMonth, sourceYear);
        if (source == null) {
            throw new IllegalArgumentException("No source BudgetConfig found for provided month/year");
        }

        // check target doesn't already have config — we will not overwrite existing target config
        BudgetConfig existingTarget = budgetConfigRepositoryService.findByProfileIdAndMonthAndYear(profileId, targetMonth, targetYear);
        if (existingTarget != null) {
            throw new IllegalArgumentException("Target month/year already has configuration. Please delete it before inheriting.");
        }

        // --- Clone BudgetConfig (root) ---
        BudgetConfig targetConfig = new BudgetConfig();
        // copy primitive fields
        targetConfig.setProfile(source.getProfile());
        targetConfig.setMonth(targetMonth);
        targetConfig.setYear(targetYear);
        targetConfig.setActualSalary(source.getActualSalary());
        targetConfig.setBudgetPercentage(source.getBudgetPercentage());
        targetConfig.setBudgetSalary(source.getBudgetSalary());
        // spent/balance reset to source values (keeps same spent / balance) OR set to zero as preferred.
        // We'll zero out spent and set balance equal to budget salary to make it "fresh"
        targetConfig.setSpentSalary(BigDecimal.ZERO);
        targetConfig.setBalanceSalary(targetConfig.getBudgetSalary() != null ? targetConfig.getBudgetSalary() : BigDecimal.ZERO);
        // metadata
        targetConfig.setCreatedUser(source.getCreatedUser());
        targetConfig.setUpdatedUser(source.getUpdatedUser());
        targetConfig.setCreatedDate(LocalDateTime.now());
        targetConfig.setUpdatedDate(LocalDateTime.now());
        // Save root first to get ID
        targetConfig = budgetConfigRepositoryService.save(targetConfig);

        // --- Clone BudgetSections ---
        List<BudgetSection> sourceSections = source.getSections() != null ? source.getSections() : Collections.emptyList();
        Map<Long, Long> sectionIdMap = new HashMap<>(); // map sourceSectionId -> newSectionId

        List<BudgetSection> savedSections = new ArrayList<>();
        for (BudgetSection s : sourceSections) {
            BudgetSection ns = new BudgetSection();
            ns.setBudgetConfig(targetConfig);
            ns.setSectionName(s.getSectionName());
            ns.setSectionPercentage(s.getSectionPercentage());
            ns.setAllottedAmount(s.getAllottedAmount());
            ns.setSpentAmount(BigDecimal.ZERO); // reset spent for fresh month
            ns.setBalanceAmount(ns.getAllottedAmount() != null ? ns.getAllottedAmount() : BigDecimal.ZERO);

            ns.setCreatedUser(s.getCreatedUser());
            ns.setUpdatedUser(s.getUpdatedUser());
            ns.setCreatedDate(LocalDateTime.now());
            ns.setUpdatedDate(LocalDateTime.now());

            ns = budgetSectionRepositorySevice.save(ns);
            sectionIdMap.put(s.getId(), ns.getId());
            savedSections.add(ns);
        }

        // --- Clone FinanceType -> FinanceCategory -> FinanceDetail ---
        // For each original financeType belonging to source sections, clone with references to new section
        List<FinanceType> sourceFinanceTypes = financeTypeRepositoryService.findAll().stream()
                .filter(ft -> ft.getBudgetConfig() != null && ft.getBudgetConfig().getId().equals(source.getId()))
                .collect(Collectors.toList());

        Map<Long, Long> financeTypeIdMap = new HashMap<>();
        Map<Long, Long> financeCategoryIdMap = new HashMap<>();

        for (FinanceType sourceType : sourceFinanceTypes) {
            // new type belongs to the new section
            Long srcSectionId = sourceType.getBudgetSection().getId();
            Long newSectionId = sectionIdMap.get(srcSectionId);
            if (newSectionId == null) {
                // skip if matching section not found (should not happen)
                continue;
            }
            BudgetSection newSection = budgetSectionRepositorySevice.findById(newSectionId);
            if (newSection == null) continue;

            FinanceType newType = new FinanceType();
            newType.setBudgetConfig(targetConfig);
            newType.setBudgetSection(newSection);
            newType.setTypeName(sourceType.getTypeName());
            newType.setTypePercentage(sourceType.getTypePercentage());
            newType.setAllottedAmount(sourceType.getAllottedAmount());
            newType.setSpentAmount(BigDecimal.ZERO);
            newType.setBalanceAmount(newType.getAllottedAmount() != null ? newType.getAllottedAmount() : BigDecimal.ZERO);

            newType.setCreatedUser(sourceType.getCreatedUser());
            newType.setUpdatedUser(sourceType.getUpdatedUser());
            newType.setCreatedDate(LocalDateTime.now());
            newType.setUpdatedDate(LocalDateTime.now());

            newType = financeTypeRepositoryService.save(newType);
            financeTypeIdMap.put(sourceType.getId(), newType.getId());

            // Now clone categories for this type
            List<FinanceCategory> categories = sourceType.getCategories() != null ? sourceType.getCategories() : Collections.emptyList();
            for (FinanceCategory srcCat : categories) {
                FinanceCategory newCat = new FinanceCategory();
                newCat.setFinanceType(newType);
                newCat.setCategoryName(srcCat.getCategoryName());
                newCat.setCategoryPercentage(srcCat.getCategoryPercentage());
                newCat.setAllottedAmount(srcCat.getAllottedAmount());
                newCat.setSpentAmount(BigDecimal.ZERO);
                newCat.setBalanceAmount(newCat.getAllottedAmount() != null ? newCat.getAllottedAmount() : BigDecimal.ZERO);

                newCat.setCreatedUser(srcCat.getCreatedUser());
                newCat.setUpdatedUser(srcCat.getUpdatedUser());
                newCat.setCreatedDate(LocalDateTime.now());
                newCat.setUpdatedDate(LocalDateTime.now());

                newCat = financeCategoryRepositoryService.save(newCat);
                financeCategoryIdMap.put(srcCat.getId(), newCat.getId());

                // Clone details under category
                List<FinanceDetail> details = srcCat.getDetails() != null ? srcCat.getDetails() : Collections.emptyList();
                for (FinanceDetail srcDet : details) {
                    FinanceDetail newDet = new FinanceDetail();
                    newDet.setFinanceCategory(newCat);
                    newDet.setDetailName(srcDet.getDetailName());
                    newDet.setDetailPercentage(srcDet.getDetailPercentage());
                    newDet.setAllottedAmount(srcDet.getAllottedAmount());
                    newDet.setSpentAmount(BigDecimal.ZERO);
                    newDet.setBalanceAmount(newDet.getAllottedAmount() != null ? newDet.getAllottedAmount() : BigDecimal.ZERO);

                    newDet.setCreatedUser(srcDet.getCreatedUser());
                    newDet.setUpdatedUser(srcDet.getUpdatedUser());
                    newDet.setCreatedDate(LocalDateTime.now());
                    newDet.setUpdatedDate(LocalDateTime.now());

                    financeDetailRepositoryService.save(newDet);
                }
            }
        }

        // --- Clone master_config rows for profile where configName is month/year-specific or global values —
        // The user asked to include master_config based on month/year-specific rows.
        // We will copy any MasterConfig for the profile where month/year match sourceMonth/sourceYear OR global (month/year null) but only if month/year specific for "target" makes sense.
        List<MasterConfig> sourceMasterConfigs = masterConfigRepositoryService.getConfigsByProfile(profileId).stream()
                .filter(mc -> {
                    // include those which are global (null month/year) OR specifically tied to source month/year
                    if (mc.getMonth() == null && mc.getYear() == null) return false; // skip global -> user wanted month/year-specific rows only (as you said yes based on month/year-specific rows)
                    if (mc.getMonth() != null && mc.getYear() != null &&
                            mc.getMonth().equals(sourceMonth) && mc.getYear().equals(sourceYear)) return true;
                    return false;
                }).collect(Collectors.toList());

        for (MasterConfig srcMc : sourceMasterConfigs) {
            MasterConfig newMc = new MasterConfig();
            newMc.setProfile(srcMc.getProfile());
            newMc.setConfigName(srcMc.getConfigName());
            newMc.setConfigValue(srcMc.getConfigValue());
            newMc.setDescription(srcMc.getDescription());
            newMc.setMonth(targetMonth);
            newMc.setYear(targetYear);
            newMc.setCreatedUser(srcMc.getCreatedUser());
            newMc.setUpdatedUser(srcMc.getUpdatedUser());
            newMc.setActive(srcMc.getActive() != null ? srcMc.getActive() : true);
            newMc.setCreatedDate(LocalDateTime.now());
            newMc.setUpdatedDate(LocalDateTime.now());

            masterConfigRepositoryService.save(newMc);
        }

        // populate and return BudgetConfigDTO for the new target config
        BudgetConfig created = budgetConfigRepositoryService.findById(targetConfig.getId());
        if (created == null) {
            throw new RuntimeException("Failed to read back created budget config");
        }

        BudgetConfigDTO dto = populateUtils.populateBudgetConfigDTOFromBudgetConfigEntity(created);
        return dto;
    }

}
