package com.murali.mrFinMate.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.murali.mrFinMate.entity.FinanceCategory;

@Repository
public interface FinanceCategoryRepository extends JpaRepository<FinanceCategory, Long> {
    List<FinanceCategory> findByFinanceTypeId(Long financeTypeId);
}
