package com.murali.mrFinMate.repository.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	
	
}
