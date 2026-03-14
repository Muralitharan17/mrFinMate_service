package com.murali.mrFinMate.repository.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.murali.mrFinMate.dto.ExpenseFilterRequestDTO;
import com.murali.mrFinMate.entity.ExpenseTracker;
import com.murali.mrFinMate.repository.ExpenseTrackerRepository;
import com.murali.mrFinMate.utils.CommonUtils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class ExpenseTrackerRepositoryService {
	
	@Autowired
	private ExpenseTrackerRepository expenseTrackerRepository;
	
	@PersistenceContext
    private EntityManager em;
	
	@Autowired
	CommonUtils commonUtils;

	public ExpenseTracker save(ExpenseTracker expense) {
		return expenseTrackerRepository.save(expense);
	}

	public Optional<ExpenseTracker> findById(Long id) {
		return expenseTrackerRepository.findById(id);
	}

	public void deleteById(Long id) {
		expenseTrackerRepository.deleteById(id);
		
	}

	public List<ExpenseTracker> findByProfileIdAndMonthAndYearAndSectionTypeNotIn(Long profileId, String month, String year, List<String> sectionTypes) {
		return expenseTrackerRepository.findByProfileIdAndMonthAndYearAndSectionTypeNotIn(profileId, month, year, sectionTypes);
	}
	
	public List<ExpenseTracker> filterExpenses(ExpenseFilterRequestDTO expenseFilterRequestDTO) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ExpenseTracker> query = cb.createQuery(ExpenseTracker.class);
        Root<ExpenseTracker> root = query.from(ExpenseTracker.class);
        
        List<Predicate> predicates = new ArrayList<>();
        
     // Mandatory filters
        predicates.add(cb.equal(root.get("profileId"), expenseFilterRequestDTO.getProfileId()));
        
		if (!(commonUtils.isNotNull(expenseFilterRequestDTO.getFilter().getSingleDate()) || 
				(expenseFilterRequestDTO.getFilter() != null && commonUtils.isNotNull(expenseFilterRequestDTO.getFilter().getFromDate()))
				)) {
			if(!(commonUtils.isNotNull(expenseFilterRequestDTO.getMonth()) && expenseFilterRequestDTO.getMonth().equalsIgnoreCase("All"))) {
				predicates.add(cb.equal(root.get("month"), expenseFilterRequestDTO.getMonth()));
			}
		}
		
		if (!(commonUtils.isNotNull(expenseFilterRequestDTO.getFilter().getSingleDate()) || 
				(expenseFilterRequestDTO.getFilter() != null && commonUtils.isNotNull(expenseFilterRequestDTO.getFilter().getFromDate()))
				)) {
			if(!(commonUtils.isNotNull(expenseFilterRequestDTO.getYear()) && expenseFilterRequestDTO.getYear().equalsIgnoreCase("All"))) {
				predicates.add(cb.equal(root.get("year"), expenseFilterRequestDTO.getYear()));
			}
		}
		
        
     // Single date filter
        if (commonUtils.isNotNull(expenseFilterRequestDTO.getFilter().getSingleDate())) {
            predicates.add(cb.equal(root.get("date"), LocalDate.parse(expenseFilterRequestDTO.getFilter().getSingleDate())));
        }  else {

            // Date From
            if (expenseFilterRequestDTO.getFilter() != null && commonUtils.isNotNull(expenseFilterRequestDTO.getFilter().getFromDate())) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("date"), LocalDate.parse(expenseFilterRequestDTO.getFilter().getFromDate())));
            }

            // Date To
            if (expenseFilterRequestDTO.getFilter() != null && commonUtils.isNotNull(expenseFilterRequestDTO.getFilter().getToDate())) {
                predicates.add(cb.lessThanOrEqualTo(root.get("date"), LocalDate.parse(expenseFilterRequestDTO.getFilter().getToDate())));
            }
        }
        
        // Transaction types
        if (expenseFilterRequestDTO.getFilter() != null && expenseFilterRequestDTO.getFilter().getTransactionTypes() != null && !expenseFilterRequestDTO.getFilter().getTransactionTypes().isEmpty()) {
            predicates.add(root.get("transactionType").in(expenseFilterRequestDTO.getFilter().getTransactionTypes()));
        }
        
     // Section types
        if (expenseFilterRequestDTO.getFilter() != null && expenseFilterRequestDTO.getFilter().getSectionTypes() != null && !expenseFilterRequestDTO.getFilter().getSectionTypes().isEmpty()) {
            predicates.add(root.get("sectionType").in(expenseFilterRequestDTO.getFilter().getSectionTypes()));
        }

        // Finance types
        if (expenseFilterRequestDTO.getFilter() != null && expenseFilterRequestDTO.getFilter().getFinanceTypes() != null && !expenseFilterRequestDTO.getFilter().getFinanceTypes().isEmpty()) {
            predicates.add(root.get("financeType").in(expenseFilterRequestDTO.getFilter().getFinanceTypes()));
        }

        // Categories
        if (expenseFilterRequestDTO.getFilter() != null && expenseFilterRequestDTO.getFilter().getCategories() != null && !expenseFilterRequestDTO.getFilter().getCategories().isEmpty()) {
            predicates.add(root.get("financeCategory").in(expenseFilterRequestDTO.getFilter().getCategories()));
        }

        // Details
        if (expenseFilterRequestDTO.getFilter() != null && expenseFilterRequestDTO.getFilter().getDetails() != null && !expenseFilterRequestDTO.getFilter().getDetails().isEmpty()) {
            predicates.add(root.get("financeDetail").in(expenseFilterRequestDTO.getFilter().getDetails()));
        }
        
     // Min Amount
        if (expenseFilterRequestDTO.getFilter() != null  &&  expenseFilterRequestDTO.getFilter().getMinAmount() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("amount"), expenseFilterRequestDTO.getFilter().getMinAmount()));
        }

        // Max Amount
        if (expenseFilterRequestDTO.getFilter() != null  && expenseFilterRequestDTO.getFilter().getMaxAmount() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("amount"), expenseFilterRequestDTO.getFilter().getMaxAmount()));
        }
        
		if (expenseFilterRequestDTO.getFilter() != null && commonUtils.isNotNull(expenseFilterRequestDTO.getFilter().getRemark())) {
			predicates.add(cb.like(cb.lower(root.get("remark")), "%" + expenseFilterRequestDTO.getFilter().getRemark().toLowerCase() + "%"));
		}
        
        query.where(predicates.toArray(new Predicate[0]));
        query.orderBy(cb.desc(root.get("date")), cb.desc(root.get("id")));

        return em.createQuery(query).getResultList();
	}

	public List<ExpenseTracker> findByProfileIdAndSectionTypeNotIn(Long profileId, List<String> sectionTypes) {
		return expenseTrackerRepository.findByProfileIdAndSectionTypeNotIn(profileId, sectionTypes);
	}

	public List<ExpenseTracker> findByProfileIdAndYearAndSectionTypeNotIn(Long profileId, String year, List<String> sectionTypes) {
		return expenseTrackerRepository.findByProfileIdAndYearAndSectionTypeNotIn(profileId, year, sectionTypes);
	}

	public List<ExpenseTracker> findByProfileIdAndMonthAndSectionTypeNotIn(Long profileId, String month, List<String> sectionTypes) {
		return expenseTrackerRepository.findByProfileIdAndMonthAndSectionTypeNotIn(profileId, month, sectionTypes);
	}

	public ExpenseTracker findFirstByProfileIdAndMonthAndYearOrderByIdDesc(Long profileId, String month, String year) {
		return expenseTrackerRepository.findFirstByProfileIdAndMonthAndYearOrderByIdDesc(profileId, month, year);
	}

	public List<ExpenseTracker> findByProfileIdAndDateGreaterThanEqualAndSectionTypeNotInOrderByDateAscIdAsc(Long profileId,
			LocalDate affectedDate, List<String> excludedSectionTypes) {
		return expenseTrackerRepository.findByProfileIdAndDateGreaterThanEqualAndSectionTypeNotInOrderByDateAscIdAsc(profileId, affectedDate, excludedSectionTypes);
	}

	public void saveAll(List<ExpenseTracker> records) {
		expenseTrackerRepository.saveAll(records);
	}

	public Optional<ExpenseTracker> findTopByProfileIdAndDateLessThanAndSectionTypeNotInOrderByDateDescIdDesc(Long profileId,
			LocalDate date, List<String> excludedSectionTypes) {
		return expenseTrackerRepository.findTopByProfileIdAndDateLessThanAndSectionTypeNotInOrderByDateDescIdDesc(profileId, date, excludedSectionTypes);
	}

}
