package com.murali.mrFinMate.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.murali.mrFinMate.dto.BudgetSectionDTO;
import com.murali.mrFinMate.entity.BudgetSection;

public interface BudgetSectionRepository extends JpaRepository<BudgetSection, Long> {

	Optional<BudgetSection> findByBudgetConfig_IdAndSectionNameIgnoreCase(Long budgetConfigId, String sectionName);

	@Query("""
		    SELECT new com.murali.mrFinMate.dto.BudgetSectionDTO(
		        s.sectionName,
		        SUM(s.sectionPercentage),
		        SUM(s.allottedAmount),
		        SUM(s.spentAmount),
		        SUM(s.balanceAmount)
		    )
		    FROM BudgetConfig b
		    JOIN b.sections s
		    WHERE b.profile.id = :profileId
		    GROUP BY s.sectionName
		""")
	List<BudgetSectionDTO> sumAllSectionsForProfile(Long profileId);

	@Query("""
		    SELECT new com.murali.mrFinMate.dto.BudgetSectionDTO(
		        s.sectionName,
		        SUM(s.sectionPercentage),
		        SUM(s.allottedAmount),
		        SUM(s.spentAmount),
		        SUM(s.balanceAmount)
		    )
		    FROM BudgetConfig b
		    JOIN b.sections s
		    WHERE b.profile.id = :profileId AND b.year = :year
		    GROUP BY s.sectionName
		""")
	List<BudgetSectionDTO> sumSectionsForYear(Long profileId, String year);

	@Query("""
		    SELECT new com.murali.mrFinMate.dto.BudgetSectionDTO(
		        s.sectionName,
		        SUM(s.sectionPercentage),
		        SUM(s.allottedAmount),
		        SUM(s.spentAmount),
		        SUM(s.balanceAmount)
		    )
		    FROM BudgetConfig b
		    JOIN b.sections s
		    WHERE b.profile.id = :profileId AND b.month = :month
		    GROUP BY s.sectionName
		""")
	List<BudgetSectionDTO> sumSectionsForMonthAcrossYears(Long profileId, String month);
}

