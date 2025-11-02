package com.murali.mrFinMate.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.murali.mrFinMate.entity.BudgetConfig;

public interface BudgetConfigRepository extends JpaRepository<BudgetConfig, Long> {
	
	Optional<BudgetConfig> findByProfile_IdAndMonthAndYear(Long profileId, String month, String year);
}

