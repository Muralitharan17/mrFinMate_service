package com.murali.mrFinMate.repository.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.murali.mrFinMate.dto.FinanceTypeDTO;
import com.murali.mrFinMate.entity.FinanceType;
import com.murali.mrFinMate.repository.FinanceTypeRepository;

@Service
public class FinanceTypeRepositoryService {

	@Autowired
	FinanceTypeRepository financeTypeRepository;

	public List<FinanceType> findByBudgetSectionId(Long sectionId) {
		return financeTypeRepository.findByBudgetSection_Id(sectionId);
	}

	public void delete(FinanceType t) {
		financeTypeRepository.delete(t);
	}

	public void saveAll(List<FinanceType> updatedTypes) {
		financeTypeRepository.saveAll(updatedTypes);
	}

	public FinanceType save(FinanceType financeType) {
		return financeTypeRepository.save(financeType);
	}

	public Optional<FinanceType> findByBudgetConfig_IdAndSectionIdAndFinanceTypeName(Long id, Long id2,
			String financeType) {
		return financeTypeRepository.findByBudgetConfig_IdAndBudgetSection_IdAndTypeName(id, id2, financeType);
	}

	public List<FinanceType> findAll() {
		return financeTypeRepository.findAll();
	}

	public List<FinanceTypeDTO> aggregateTypesBasedOnProfileId(Long profileId, String sectionName) {
		return financeTypeRepository.aggregateTypesBasedOnProfileId(profileId, sectionName);
	}

	public List<FinanceTypeDTO> aggregateTypesBasedOnYear(String year, String sectionName) {
		return financeTypeRepository.aggregateTypesBasedOnYear(year, sectionName);
	}

	public List<FinanceTypeDTO> aggregateTypesBasedOnMonth(String month, String sectionName) {
		return financeTypeRepository.aggregateTypesBasedOnMonth(month, sectionName);
	}
	
	
}
