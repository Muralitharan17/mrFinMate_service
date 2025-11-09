package com.murali.mrFinMate.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.murali.mrFinMate.entity.FinanceType;

@Repository
public interface FinanceTypeRepository extends JpaRepository<FinanceType, Long> {
    List<FinanceType> findByBudgetSection_Id(Long sectionId);
}
