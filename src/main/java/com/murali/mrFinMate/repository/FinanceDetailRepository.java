package com.murali.mrFinMate.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.murali.mrFinMate.entity.FinanceDetail;

@Repository
public interface FinanceDetailRepository extends JpaRepository<FinanceDetail, Long> {
    List<FinanceDetail> findByFinanceCategoryId(Long financeCategoryId);

	Optional<FinanceDetail> findByFinanceCategory_IdAndDetailName(Long id, String detail);
}
