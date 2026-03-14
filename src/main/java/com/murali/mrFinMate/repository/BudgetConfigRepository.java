package com.murali.mrFinMate.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.murali.mrFinMate.dto.BudgetConfigDTO;
import com.murali.mrFinMate.dto.BudgetSectionDTO;
import com.murali.mrFinMate.entity.BudgetConfig;

public interface BudgetConfigRepository extends JpaRepository<BudgetConfig, Long> {

	Optional<BudgetConfig> findByProfile_IdAndMonthAndYear(Long profileId, String month, String year);

	@Query("""
			    SELECT new com.murali.mrFinMate.dto.BudgetConfigDTO(
			    b.profile.id,
			    "ALL",
			    "ALL",
			      SUM(b.actualSalary),
			      SUM(b.spentSalary),
			      SUM(b.balanceSalary),
			      SUM(b.budgetSalary),
			      SUM(b.budgetPercentage)
			  )
			  FROM BudgetConfig b
			  WHERE b.profile.id = :profileId
			""")
	BudgetConfigDTO sumAllBudgetForProfile(Long profileId);

	@Query("""
			    SELECT new com.murali.mrFinMate.dto.BudgetConfigDTO(
			    b.profile.id,
			    "ALL",
			   b.year,
			      SUM(b.actualSalary),
			      SUM(b.spentSalary),
			      SUM(b.balanceSalary),
			      SUM(b.budgetSalary),
			      SUM(b.budgetPercentage)
			  )
			  FROM BudgetConfig b
			    WHERE b.profile.id = :profileId AND b.year = :year
			""")
	BudgetConfigDTO sumBudgetForYear(Long profileId, String year);

	@Query("""
			    SELECT new com.murali.mrFinMate.dto.BudgetConfigDTO(
			    b.profile.id,
			    b.month,
			   "ALL",
			      SUM(b.actualSalary),
			      SUM(b.spentSalary),
			      SUM(b.balanceSalary),
			      SUM(b.budgetSalary),
			      SUM(b.budgetPercentage)
			  )
			  FROM BudgetConfig b
			    WHERE b.profile.id = :profileId AND b.month = :month
			""")
	BudgetConfigDTO sumBudgetForMonthAcrossYears(Long profileId, String month);

}
