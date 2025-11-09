package com.murali.mrFinMate.repository.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.murali.mrFinMate.entity.FinanceCategory;
import com.murali.mrFinMate.repository.FinanceCategoryRepository;

@Service
public class FinanceCategoryRepositoryService {

	@Autowired
	FinanceCategoryRepository financeCategoryRepository;

	public void delete(FinanceCategory c) {
		financeCategoryRepository.delete(c);
	}
}
