package com.murali.mrFinMate.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.murali.mrFinMate.dto.FinanceDetailDTO;
import com.murali.mrFinMate.entity.FinanceDetail;

@Repository
public interface FinanceDetailRepository extends JpaRepository<FinanceDetail, Long> {
    List<FinanceDetail> findByFinanceCategoryId(Long financeCategoryId);

	Optional<FinanceDetail> findByFinanceCategory_IdAndDetailName(Long id, String detail);

	@Query("""
	        SELECT new com.murali.mrFinMate.dto.FinanceDetailDTO(
	            ft.typeName,
	            fc.categoryName,
	            fd.detailName,
	            SUM(fd.detailPercentage),
	            SUM(fd.allottedAmount),
	            SUM(fd.spentAmount),
	            SUM(fd.balanceAmount)
	        )
	        FROM FinanceDetail fd
	        JOIN fd.financeCategory fc
	        JOIN fc.financeType ft
	        WHERE ft.budgetConfig.profile.id = :profileId
	            AND ft.budgetSection.sectionName = :sectionName
	        GROUP BY ft.typeName, fc.categoryName, fd.detailName
	    """)
	List<FinanceDetailDTO> aggregateTypesBasedOnProfileId(Long profileId, String sectionName);

	@Query("""
	        SELECT new com.murali.mrFinMate.dto.FinanceDetailDTO(
	            ft.typeName,
	            fc.categoryName,
	            fd.detailName,
	            SUM(fd.detailPercentage),
	            SUM(fd.allottedAmount),
	            SUM(fd.spentAmount),
	            SUM(fd.balanceAmount)
	        )
	        FROM FinanceDetail fd
	        JOIN fd.financeCategory fc
	        JOIN fc.financeType ft
	        WHERE ft.budgetConfig.year = :year
			    AND ft.budgetSection.sectionName = :sectionName
	        GROUP BY ft.typeName, fc.categoryName, fd.detailName
	    """)
	List<FinanceDetailDTO> aggregateTypesBasedOnYear(String year, String sectionName);

	@Query("""
	        SELECT new com.murali.mrFinMate.dto.FinanceDetailDTO(
	            ft.typeName,
	            fc.categoryName,
	            fd.detailName,
	            SUM(fd.detailPercentage),
	            SUM(fd.allottedAmount),
	            SUM(fd.spentAmount),
	            SUM(fd.balanceAmount)
	        )
	        FROM FinanceDetail fd
	        JOIN fd.financeCategory fc
	        JOIN fc.financeType ft
	        WHERE ft.budgetConfig.month = :month
	            AND ft.budgetSection.sectionName = :sectionName
	        GROUP BY ft.typeName, fc.categoryName, fd.detailName
	    """)
	List<FinanceDetailDTO> aggregateTypesBasedOnMonth(String month, String sectionName);
	
	
}
