package com.murali.mrFinMate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.murali.mrFinMate.entity.ExpenseTracker;
import java.util.List;

@Repository
public interface ExpenseTrackerRepository extends JpaRepository<ExpenseTracker, Long> {

    List<ExpenseTracker> findByProfileIdAndMonthAndYear(Long profileId, String month, String year);

    List<ExpenseTracker> findByProfileIdAndMonthAndYearAndSectionType(Long profileId, String month, String year, String sectionType);

    List<ExpenseTracker> findByProfileIdAndMonthAndYearAndSectionTypeAndFinanceType(Long profileId, String month, String year, String sectionType, String financeType);

    List<ExpenseTracker> findByProfileIdAndMonthAndYearAndSectionTypeAndFinanceTypeAndFinanceCategory(Long profileId, String month, String year, String sectionType, String financeType, String category);

    List<ExpenseTracker> findByProfileIdAndMonthAndYearAndSectionTypeAndFinanceTypeAndFinanceCategoryAndFinanceDetail(Long profileId, String month, String year, String sectionType, String financeType, String category, String detail);

}
