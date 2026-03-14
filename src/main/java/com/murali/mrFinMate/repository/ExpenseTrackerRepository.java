package com.murali.mrFinMate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.murali.mrFinMate.entity.ExpenseTracker;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ExpenseTrackerRepository extends JpaRepository<ExpenseTracker, Long> {

    List<ExpenseTracker> findByProfileIdAndMonthAndYearAndSectionTypeNotIn(Long profileId, String month, String year, List<String> sectionTypes);

    List<ExpenseTracker> findByProfileIdAndMonthAndYearAndSectionType(Long profileId, String month, String year, String sectionType);

    List<ExpenseTracker> findByProfileIdAndMonthAndYearAndSectionTypeAndFinanceType(Long profileId, String month, String year, String sectionType, String financeType);

    List<ExpenseTracker> findByProfileIdAndMonthAndYearAndSectionTypeAndFinanceTypeAndFinanceCategory(Long profileId, String month, String year, String sectionType, String financeType, String category);

    List<ExpenseTracker> findByProfileIdAndMonthAndYearAndSectionTypeAndFinanceTypeAndFinanceCategoryAndFinanceDetail(Long profileId, String month, String year, String sectionType, String financeType, String category, String detail);

	List<ExpenseTracker> findByProfileIdAndSectionTypeNotIn(Long profileId, List<String> sectionTypes);

	List<ExpenseTracker> findByProfileIdAndYearAndSectionTypeNotIn(Long profileId, String year, List<String> sectionTypes);

	List<ExpenseTracker> findByProfileIdAndMonthAndSectionTypeNotIn(Long profileId, String month, List<String> sectionTypes);
	
	ExpenseTracker findFirstByProfileIdAndMonthAndYearOrderByIdDesc(Long profileId, String month, String year);

	List<ExpenseTracker> findByProfileIdAndDateGreaterThanEqualAndSectionTypeNotInOrderByDateAscIdAsc(Long profileId,
			LocalDate affectedDate, List<String> excludedSectionTypes);

	Optional<ExpenseTracker> findTopByProfileIdAndDateLessThanAndSectionTypeNotInOrderByDateDescIdDesc(Long profileId, LocalDate date, List<String> excludedSectionTypes);

}
