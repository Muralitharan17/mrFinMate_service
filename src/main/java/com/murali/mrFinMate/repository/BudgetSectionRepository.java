package com.murali.mrFinMate.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.murali.mrFinMate.entity.BudgetSection;

public interface BudgetSectionRepository extends JpaRepository<BudgetSection, Long> {

	Optional<BudgetSection> findByBudgetConfig_IdAndSectionNameIgnoreCase(Long budgetConfigId, String sectionName);
}

