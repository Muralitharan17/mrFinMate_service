package com.murali.mrFinMate.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.murali.mrFinMate.dto.FinanceCategoryDTO;
import com.murali.mrFinMate.entity.FinanceCategory;

@Repository
public interface FinanceCategoryRepository extends JpaRepository<FinanceCategory, Long> {
    List<FinanceCategory> findByFinanceTypeId(Long financeTypeId);

	Optional<FinanceCategory> findByFinanceType_IdAndCategoryName(Long id, String category);

	@Query("""
	        SELECT new com.murali.mrFinMate.dto.FinanceCategoryDTO(
	            ft.typeName,
	            fc.categoryName,
	            SUM(fc.categoryPercentage),
	            SUM(fc.allottedAmount),
	            SUM(fc.spentAmount),
	            SUM(fc.balanceAmount)
	        )
	        FROM FinanceCategory fc
	        JOIN fc.financeType ft
	        WHERE ft.budgetConfig.profile.id = :profileId
	            AND ft.budgetSection.sectionName = :sectionName
	        GROUP BY ft.typeName, fc.categoryName
	    """)
	List<FinanceCategoryDTO> aggregateTypesBasedOnProfileId(Long profileId, String sectionName);

	@Query("""
	        SELECT new com.murali.mrFinMate.dto.FinanceCategoryDTO(
	            ft.typeName,
	            fc.categoryName,
	            SUM(fc.categoryPercentage),
	            SUM(fc.allottedAmount),
	            SUM(fc.spentAmount),
	            SUM(fc.balanceAmount)
	        )
	        FROM FinanceCategory fc
	        JOIN fc.financeType ft
	        WHERE ft.budgetConfig.year = :year
	            AND ft.budgetSection.sectionName = :sectionName
	        GROUP BY ft.typeName, fc.categoryName
	    """)
	List<FinanceCategoryDTO> aggregateTypesBasedOnYear(String year, String sectionName);

	@Query("""
	        SELECT new com.murali.mrFinMate.dto.FinanceCategoryDTO(
	            ft.typeName,
	            fc.categoryName,
	            SUM(fc.categoryPercentage),
	            SUM(fc.allottedAmount),
	            SUM(fc.spentAmount),
	            SUM(fc.balanceAmount)
	        )
	        FROM FinanceCategory fc
	        JOIN fc.financeType ft
	        WHERE ft.budgetConfig.month = :month
	            AND ft.budgetSection.sectionName = :sectionName
	        GROUP BY ft.typeName, fc.categoryName
	    """)
	List<FinanceCategoryDTO> aggregateTypesBasedOnMonth(String month, String sectionName);
	
}
