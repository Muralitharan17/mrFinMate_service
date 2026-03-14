package com.murali.mrFinMate.repository.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.murali.mrFinMate.dto.FinanceDetailDTO;
import com.murali.mrFinMate.entity.FinanceDetail;
import com.murali.mrFinMate.repository.FinanceDetailRepository;

@Service
public class FinanceDetailRepositoryService {

	@Autowired
	FinanceDetailRepository financeDetailRepository;

	public void delete(FinanceDetail d) {
		financeDetailRepository.delete(d);
	}

	public Optional<FinanceDetail> findByFinanceCategory_IdAndFinanceDetailName(Long id, String detail) {
		return financeDetailRepository.findByFinanceCategory_IdAndDetailName(id, detail);
	}

	public void save(FinanceDetail fd) {
        financeDetailRepository.save(fd);		
	}

	public List<FinanceDetailDTO> aggregateTypesBasedOnProfileId(Long profileId, String sectionName) {
		return financeDetailRepository.aggregateTypesBasedOnProfileId(profileId, sectionName);
	}

	public List<FinanceDetailDTO> aggregateTypesBasedOnYear(String year, String sectionName) {
		return financeDetailRepository.aggregateTypesBasedOnYear(year, sectionName);
	}

	public List<FinanceDetailDTO> aggregateTypesBasedOnMonth(String month, String sectionName) {
		return financeDetailRepository.aggregateTypesBasedOnMonth(month, sectionName);
	}
	
	
}
