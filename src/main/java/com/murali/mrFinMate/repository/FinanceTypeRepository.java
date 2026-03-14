package com.murali.mrFinMate.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.murali.mrFinMate.dto.FinanceTypeDTO;
import com.murali.mrFinMate.entity.FinanceType;

@Repository
public interface FinanceTypeRepository extends JpaRepository<FinanceType, Long> {
    List<FinanceType> findByBudgetSection_Id(Long sectionId);

	Optional<FinanceType> findByBudgetConfig_IdAndBudgetSection_IdAndTypeName(Long id, Long id2, String financeType);

	@Query("""
		    SELECT new com.murali.mrFinMate.dto.FinanceTypeDTO(
		        ft.typeName,
		        SUM(ft.typePercentage),
		        SUM(ft.allottedAmount),
		        SUM(ft.spentAmount),
		        SUM(ft.balanceAmount)
		    )
		    FROM FinanceType ft
		    WHERE ft.budgetConfig.profile.id = :profileId
		        AND ft.budgetSection.sectionName = :sectionName
		    GROUP BY ft.typeName
		""")
	List<FinanceTypeDTO> aggregateTypesBasedOnProfileId(Long profileId, String sectionName);

	@Query("""
		    SELECT new com.murali.mrFinMate.dto.FinanceTypeDTO(
		        ft.typeName,
		        SUM(ft.typePercentage),
		        SUM(ft.allottedAmount),
		        SUM(ft.spentAmount),
		        SUM(ft.balanceAmount)
		    )
		    FROM FinanceType ft
		    WHERE ft.budgetConfig.year = :year
			    AND ft.budgetSection.sectionName = :sectionName
		    GROUP BY ft.typeName
		""")
	List<FinanceTypeDTO> aggregateTypesBasedOnYear(String year, String sectionName);

	@Query("""
		    SELECT new com.murali.mrFinMate.dto.FinanceTypeDTO(
		        ft.typeName,
		        SUM(ft.typePercentage),
		        SUM(ft.allottedAmount),
		        SUM(ft.spentAmount),
		        SUM(ft.balanceAmount)
		    )
		    FROM FinanceType ft
		    WHERE ft.budgetConfig.month = :month
			    AND ft.budgetSection.sectionName = :sectionName
		    GROUP BY ft.typeName
		""")
	List<FinanceTypeDTO> aggregateTypesBasedOnMonth(String month, String sectionName);
}
