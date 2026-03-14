package com.murali.mrFinMate.repository.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.murali.mrFinMate.dto.FinanceCategoryDTO;
import com.murali.mrFinMate.entity.FinanceCategory;
import com.murali.mrFinMate.repository.FinanceCategoryRepository;

@Service
public class FinanceCategoryRepositoryService {

	@Autowired
	FinanceCategoryRepository financeCategoryRepository;

	public void delete(FinanceCategory c) {
		financeCategoryRepository.delete(c);
	}

	public Optional<FinanceCategory> findByFinanceType_IdAndFinanceCategoryName(Long id, String category) {
		return financeCategoryRepository.findByFinanceType_IdAndCategoryName(id, category);
	}

	public FinanceCategory save(FinanceCategory fc) {
		return financeCategoryRepository.save(fc);
	}

	public List<FinanceCategoryDTO> aggregateTypesBasedOnProfileId(Long profileId, String sectionName) {
		return financeCategoryRepository.aggregateTypesBasedOnProfileId(profileId, sectionName);
	}

	public List<FinanceCategoryDTO> aggregateTypesBasedOnYear(String year, String sectionName) {
		return financeCategoryRepository.aggregateTypesBasedOnYear(year, sectionName);
	}

	public List<FinanceCategoryDTO> aggregateTypesBasedOnMonth(String month, String sectionName) {
		return financeCategoryRepository.aggregateTypesBasedOnMonth(month, sectionName);
	}
}
