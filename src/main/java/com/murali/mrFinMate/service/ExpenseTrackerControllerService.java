package com.murali.mrFinMate.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.murali.mrFinMate.constants.CommonConstants;
import com.murali.mrFinMate.dto.ExpenseFilterRequestDTO;
import com.murali.mrFinMate.entity.BudgetConfig;
import com.murali.mrFinMate.entity.BudgetSection;
import com.murali.mrFinMate.entity.ExpenseTracker;
import com.murali.mrFinMate.entity.FinanceCategory;
import com.murali.mrFinMate.entity.FinanceDetail;
import com.murali.mrFinMate.entity.FinanceType;
import com.murali.mrFinMate.repository.service.BudgetConfigRepositoryService;
import com.murali.mrFinMate.repository.service.BudgetSectionRepositorySevice;
import com.murali.mrFinMate.repository.service.ExpenseTrackerRepositoryService;
import com.murali.mrFinMate.repository.service.FinanceCategoryRepositoryService;
import com.murali.mrFinMate.repository.service.FinanceDetailRepositoryService;
import com.murali.mrFinMate.repository.service.FinanceTypeRepositoryService;
import com.murali.mrFinMate.utils.CommonUtils;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class ExpenseTrackerControllerService {

    private final CommonUtils commonUtils;

	@Autowired
      ExpenseTrackerRepositoryService expenseTrackerRepositoryService;
	
	@Autowired
      BudgetConfigRepositoryService budgetConfigRepositoryService;
	
	@Autowired
      BudgetSectionRepositorySevice budgetSectionRepositorySevice;
	
	@Autowired
      FinanceTypeRepositoryService financeTypeRepositoryService;
	
	@Autowired
      FinanceCategoryRepositoryService financeCategoryRepositoryService;
	
	@Autowired
      FinanceDetailRepositoryService financeDetailRepositoryService;

	@Autowired
	GoalConfigControllerService GoalConfigControllerService;

    ExpenseTrackerControllerService(CommonUtils commonUtils) {
        this.commonUtils = commonUtils;
    }

    @Transactional
    public ExpenseTracker saveOrUpdateExpense(ExpenseTracker expense) {
    	ExpenseTracker saved = null;
    	ExpenseTracker newOrUpdateExpense = null;
    	boolean isNewExpense = true;
    	try {
    		
    		BigDecimal amount = BigDecimal.ZERO;
    		
			if (expense != null && expense.getId() != null) {
				newOrUpdateExpense = expenseTrackerRepositoryService.findById(expense.getId())
	    	            .orElse(null);
				
				if(newOrUpdateExpense != null) {
					BigDecimal oldAmount = newOrUpdateExpense.getAmount() == null ? BigDecimal.ZERO : newOrUpdateExpense.getAmount();
	    	        BigDecimal newAmount = expense.getAmount() == null ? BigDecimal.ZERO : expense.getAmount();
	    	        BigDecimal delta = newAmount.subtract(oldAmount); // positive -> additional spent, negative -> refund
	    	        amount = delta;
	    	        
	    	        isNewExpense = false;
	    	        
	    	     // update existing row
	    	        newOrUpdateExpense.setDate(expense.getDate());
	    	        newOrUpdateExpense.setTransactionType(expense.getTransactionType());
	    	        newOrUpdateExpense.setSectionType(expense.getSectionType());
	    	        newOrUpdateExpense.setFinanceType(expense.getFinanceType());
	    	        newOrUpdateExpense.setFinanceCategory(expense.getFinanceCategory());
	    	        newOrUpdateExpense.setFinanceDetail(expense.getFinanceDetail());
	    	        newOrUpdateExpense.setAmount(newAmount);
				}
			} else {
				newOrUpdateExpense = expense;
				amount = expense.getAmount() == null ? BigDecimal.ZERO : expense.getAmount();
				
				newOrUpdateExpense.setAmount(amount);
			}

    		
			if (commonUtils.isNotNull(newOrUpdateExpense.getTransactionType())
					&& newOrUpdateExpense.getTransactionType().equalsIgnoreCase(CommonConstants.INITIAL_AMOUNT)) {
				newOrUpdateExpense.setBalance(amount);
			}
    		
    		// Save expense row first
            saved = expenseTrackerRepositoryService.save(newOrUpdateExpense);
            
            // Recalculate balance from the affected date
            recalcBalanceFromDate(newOrUpdateExpense.getProfileId(), newOrUpdateExpense.getDate());
    		
            if(commonUtils.isNull(newOrUpdateExpense.getTransactionType()) || expense.getTransactionType().equalsIgnoreCase(CommonConstants.INITIAL_AMOUNT)
            		|| !newOrUpdateExpense.getTransactionType().equalsIgnoreCase(CommonConstants.DEBIT)
            		|| commonUtils.isNull(newOrUpdateExpense.getSectionType()) || newOrUpdateExpense.getSectionType().equalsIgnoreCase(CommonConstants.GOALS)) {
            	return saved;
            } else {
            	
            	// Update budget_config.spent_salary & balance_salary
                BudgetConfig config = budgetConfigRepositoryService.findByProfileIdAndMonthAndYear(
                		newOrUpdateExpense.getProfileId(), newOrUpdateExpense.getMonth(), newOrUpdateExpense.getYear());

                config.setSpentSalary(config.getSpentSalary().add(amount));
                // balanceSalary = budgetSalary - spentSalary
                config.setBalanceSalary(config.getBudgetSalary().subtract(config.getSpentSalary()));
                budgetConfigRepositoryService.save(config);

                // Find budget_section by section name
                BudgetSection section = budgetSectionRepositorySevice.findByBudgetConfigIdAndSectionName(config.getId(), newOrUpdateExpense.getSectionType());

                // Update section spent and balance
                section.setSpentAmount(section.getSpentAmount().add(amount));
                section.setBalanceAmount(section.getAllottedAmount().subtract(section.getSpentAmount()));
                budgetSectionRepositorySevice.save(section);

                // Now update finance_type -> finance_category -> finance_detail spent amounts
                // 1) FinanceType
                Optional<FinanceType> ftOpt = financeTypeRepositoryService
                    .findByBudgetConfig_IdAndSectionIdAndFinanceTypeName(config.getId(), section.getId(), newOrUpdateExpense.getFinanceType());

                if (ftOpt.isPresent()) {
                    FinanceType ft = ftOpt.get();
                    ft.setSpentAmount(ft.getSpentAmount().add(amount));
                    // Provided BALANCE is generated in DB as ALLOTTED - SPENT, otherwise compute:
                    // ft.setBalanceAmount(ft.getAllottedAmount().subtract(ft.getSpentAmount()));
                    financeTypeRepositoryService.save(ft);

                    // 2) FinanceCategory
                    if (newOrUpdateExpense.getFinanceCategory() != null) {
                        Optional<FinanceCategory> fcOpt = financeCategoryRepositoryService
                            .findByFinanceType_IdAndFinanceCategoryName(ft.getId(), newOrUpdateExpense.getFinanceCategory());
                        if (fcOpt.isPresent()) {
                            FinanceCategory fc = fcOpt.get();
                            fc.setSpentAmount(fc.getSpentAmount().add(amount));
                            // fc.setBalanceAmount(fc.getAllottedAmount().subtract(fc.getSpentAmount()));
                            financeCategoryRepositoryService.save(fc);

                            // 3) FinanceDetail
                            if (newOrUpdateExpense.getFinanceDetail() != null) {
                                Optional<FinanceDetail> fdOpt = financeDetailRepositoryService
                                    .findByFinanceCategory_IdAndFinanceDetailName(fc.getId(), newOrUpdateExpense.getFinanceDetail());
                                if (fdOpt.isPresent()) {
                                    FinanceDetail fd = fdOpt.get();
                                    fd.setSpentAmount(fd.getSpentAmount().add(amount));
                                    // fd.setBalanceAmount(fd.getAllottedAmount().subtract(fd.getSpentAmount()));
                                    financeDetailRepositoryService.save(fd);
                                }
                            }
                        }
                    }
                    
                
                }
            	
            }
            
            
    	} catch (Exception e) {
    		e.printStackTrace();
    	} finally {
    		if(saved != null) {
    			// Need to update the Goal section
                GoalConfigControllerService.updateGoalIfMatched(saved, saved.getAmount(), false);
			} else if (expense != null) {
				GoalConfigControllerService.updateGoalIfMatched(expense, expense.getAmount(), false);
			}
    	}
        

        return saved;
    }

    @Transactional
    public void recalcBalanceFromDate(Long profileId, LocalDate affectedDate) {
    	
    	List<String> excludedSectionTypes = new ArrayList<String>();
    	excludedSectionTypes.add(CommonConstants.GOALS);

        // Fetch all future rows (including affected date)
        List<ExpenseTracker> records =
        		expenseTrackerRepositoryService.findByProfileIdAndDateGreaterThanEqualAndSectionTypeNotInOrderByDateAscIdAsc(
                        profileId, affectedDate, excludedSectionTypes);

        if (records.isEmpty()) {
            return;
        }

        // We need previous balance before affectedDate
        BigDecimal runningBalance = getBalanceBeforeDate(profileId, affectedDate, excludedSectionTypes);

        // Now run through all future records and fix balances
        for (ExpenseTracker e : records) {
            if (e.getTransactionType().equals(CommonConstants.DEBIT)) {
                runningBalance = runningBalance.subtract(e.getAmount());
            } else if (e.getTransactionType().equals(CommonConstants.CREDIT)){ 
                runningBalance = runningBalance.add(e.getAmount());
			} else if (e.getTransactionType().equals(CommonConstants.INITIAL_AMOUNT)) {
				runningBalance = e.getBalance();
			}
            e.setBalance(runningBalance);
        }

        // Save updated balances
        expenseTrackerRepositoryService.saveAll(records);
    }
    
    public BigDecimal getBalanceBeforeDate(Long profileId, LocalDate date, List<String> excludedSectionTypes) {

    	Optional<ExpenseTracker> lastRecordBefore =
    			expenseTrackerRepositoryService.findTopByProfileIdAndDateLessThanAndSectionTypeNotInOrderByDateDescIdDesc(
    	                profileId, date, excludedSectionTypes);

    	    return lastRecordBefore
    	            .map(ExpenseTracker::getBalance)
    	            .orElse(BigDecimal.ZERO);
    }

	@Transactional
    public ExpenseTracker updateExpense(Long id, ExpenseTracker updatedExpense) {
    	ExpenseTracker saved = null;
    	BigDecimal amount = BigDecimal.ZERO;
    	try {
    		
    		ExpenseTracker existing = expenseTrackerRepositoryService.findById(id)
    	            .orElseThrow(() -> new EntityNotFoundException("Expense not found"));

    	        BigDecimal oldAmount = existing.getAmount() == null ? BigDecimal.ZERO : existing.getAmount();
    	        BigDecimal newAmount = updatedExpense.getAmount() == null ? BigDecimal.ZERO : updatedExpense.getAmount();
    	        BigDecimal delta = newAmount.subtract(oldAmount); // positive -> additional spent, negative -> refund
    	        amount = delta;

    	        // update expense row
    	        existing.setDate(updatedExpense.getDate());
    	        existing.setTransactionType(updatedExpense.getTransactionType());
    	        existing.setSectionType(updatedExpense.getSectionType());
    	        existing.setFinanceType(updatedExpense.getFinanceType());
    	        existing.setFinanceCategory(updatedExpense.getFinanceCategory());
    	        existing.setFinanceDetail(updatedExpense.getFinanceDetail());
    	        existing.setAmount(newAmount);
    	        // update other fields...
    	        saved = expenseTrackerRepositoryService.save(existing);
    	        
    	        recalcBalanceFromDate(existing.getProfileId(), existing.getDate());

    	        // update budgets with delta (can be + or -)
    	        BudgetConfig config = budgetConfigRepositoryService.findByProfileIdAndMonthAndYear(
    	            existing.getProfileId(), existing.getMonth(), existing.getYear()
    	        );

    	        config.setSpentSalary(config.getSpentSalary().add(delta));
    	        config.setBalanceSalary(config.getBudgetSalary().subtract(config.getSpentSalary()));
    	        budgetConfigRepositoryService.save(config);

    	        BudgetSection section = budgetSectionRepositorySevice.findByBudgetConfigIdAndSectionName(config.getId(), existing.getSectionType());

    	        section.setSpentAmount(section.getSpentAmount().add(delta));
    	        section.setBalanceAmount(section.getAllottedAmount().subtract(section.getSpentAmount()));
    	        budgetSectionRepositorySevice.save(section);

    	        // update finance tables similarly by delta (search by names)
    	        // FinanceType
    	        financeTypeRepositoryService.findByBudgetConfig_IdAndSectionIdAndFinanceTypeName(config.getId(), section.getId(), existing.getFinanceType())
    	            .ifPresent(ft -> {
    	                ft.setSpentAmount(ft.getSpentAmount().add(delta));
    	                financeTypeRepositoryService.save(ft);
    	            });

    	        // FinanceCategory
    	        financeTypeRepositoryService.findByBudgetConfig_IdAndSectionIdAndFinanceTypeName(config.getId(), section.getId(), existing.getFinanceType())
    	            .flatMap(ft -> financeCategoryRepositoryService.findByFinanceType_IdAndFinanceCategoryName(ft.getId(), existing.getFinanceCategory()))
    	            .ifPresent(fc -> {
    	                fc.setSpentAmount(fc.getSpentAmount().add(delta));
    	                financeCategoryRepositoryService.save(fc);
    	            });

    	        // FinanceDetail
    	        financeTypeRepositoryService.findByBudgetConfig_IdAndSectionIdAndFinanceTypeName(config.getId(), section.getId(), existing.getFinanceType())
    	            .flatMap(ft -> financeCategoryRepositoryService.findByFinanceType_IdAndFinanceCategoryName(ft.getId(), existing.getFinanceCategory()))
    	            .flatMap(fc -> financeDetailRepositoryService.findByFinanceCategory_IdAndFinanceDetailName(fc.getId(), existing.getFinanceDetail()))
    	            .ifPresent(fd -> {
    	                fd.setSpentAmount(fd.getSpentAmount().add(delta));
    	                financeDetailRepositoryService.save(fd);
    	            });
    	        
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
    		if(saved != null) {
    			// Need to update the Goal section
                GoalConfigControllerService.updateGoalIfMatched(saved, amount,  false);
			} else if (updatedExpense != null) {
				GoalConfigControllerService.updateGoalIfMatched(updatedExpense, amount, false);
			}
    	}
    	

        return saved;
    }

    @Transactional
    public void deleteExpense(Long id) {
    	try {
    		ExpenseTracker existing = expenseTrackerRepositoryService.findById(id)
    	            .orElseThrow(() -> new EntityNotFoundException("Expense not found"));
    	        
    	        BigDecimal amount = existing.getAmount() == null ? BigDecimal.ZERO : existing.getAmount();
    	        existing.setAmount(amount);
    	        
    	        // Need to update the Goal section
    	        GoalConfigControllerService.updateGoalIfMatched(existing, existing.getAmount(), true);

    	        // Reverse the spent updates (subtract the expense amount)
    	        BudgetConfig config = budgetConfigRepositoryService.findByProfileIdAndMonthAndYear(
    	            existing.getProfileId(), existing.getMonth(), existing.getYear()
    	        );

    	        config.setSpentSalary(config.getSpentSalary().subtract(amount));
    	        config.setBalanceSalary(config.getBudgetSalary().subtract(config.getSpentSalary()));
    	        budgetConfigRepositoryService.save(config);

    	        BudgetSection section = budgetSectionRepositorySevice.findByBudgetConfigIdAndSectionName(config.getId(), existing.getSectionType());

    	        section.setSpentAmount(section.getSpentAmount().subtract(amount));
    	        section.setBalanceAmount(section.getAllottedAmount().subtract(section.getSpentAmount()));
    	        budgetSectionRepositorySevice.save(section);

    	        // finance_type/category/detail reverse updates
    	        financeTypeRepositoryService.findByBudgetConfig_IdAndSectionIdAndFinanceTypeName(config.getId(), section.getId(), existing.getFinanceType())
    	            .ifPresent(ft -> {
    	                ft.setSpentAmount(ft.getSpentAmount().subtract(amount));
    	                financeTypeRepositoryService.save(ft);
    	            });

    	        financeTypeRepositoryService.findByBudgetConfig_IdAndSectionIdAndFinanceTypeName(config.getId(), section.getId(), existing.getFinanceType())
    	            .flatMap(ft -> financeCategoryRepositoryService.findByFinanceType_IdAndFinanceCategoryName(ft.getId(), existing.getFinanceCategory()))
    	            .ifPresent(fc -> {
    	                fc.setSpentAmount(fc.getSpentAmount().subtract(amount));
    	                financeCategoryRepositoryService.save(fc);
    	            });

    	        financeTypeRepositoryService.findByBudgetConfig_IdAndSectionIdAndFinanceTypeName(config.getId(), section.getId(), existing.getFinanceType())
    	            .flatMap(ft -> financeCategoryRepositoryService.findByFinanceType_IdAndFinanceCategoryName(ft.getId(), existing.getFinanceCategory()))
    	            .flatMap(fc -> financeDetailRepositoryService.findByFinanceCategory_IdAndFinanceDetailName(fc.getId(), existing.getFinanceDetail()))
    	            .ifPresent(fd -> {
    	                fd.setSpentAmount(fd.getSpentAmount().subtract(amount));
    	                financeDetailRepositoryService.save(fd);
    	            });

    	        expenseTrackerRepositoryService.deleteById(id);
    	        recalcBalanceFromDate(existing.getProfileId(), existing.getDate());
    	} catch (Exception e) {
    		e.printStackTrace();
    		
    		ExpenseTracker existing = expenseTrackerRepositoryService.findById(id)
    	            .orElse(null);
    		if(existing != null) {
    			expenseTrackerRepositoryService.deleteById(id);
        		recalcBalanceFromDate(existing.getProfileId(), existing.getDate());
    		}
    	}
        
    }

    public List<ExpenseTracker> getExpenses(Long profileId, String month, String year, boolean onlyExpenses) {
    	List<String> sectionTypes = new ArrayList<String>();
    	if(onlyExpenses) {
    		sectionTypes.add(CommonConstants.GOALS);
    		sectionTypes.add(CommonConstants.INITIAL_AMOUNT);
    	}
    	
		if (commonUtils.isNotNull(month) && commonUtils.isNotNull(year) && month.equalsIgnoreCase(CommonConstants.ALL)
				&& year.equalsIgnoreCase(CommonConstants.ALL)) {
			return expenseTrackerRepositoryService.findByProfileIdAndSectionTypeNotIn(profileId, sectionTypes);
		} else if(commonUtils.isNotNull(month) && month.equalsIgnoreCase(CommonConstants.ALL)) {
    		return expenseTrackerRepositoryService.findByProfileIdAndYearAndSectionTypeNotIn(profileId, year, sectionTypes);
    	} else if (commonUtils.isNotNull(year) && year.equalsIgnoreCase(CommonConstants.ALL)) {
    		return expenseTrackerRepositoryService.findByProfileIdAndMonthAndSectionTypeNotIn(profileId, month, sectionTypes);
    	} else {
    		return expenseTrackerRepositoryService.findByProfileIdAndMonthAndYearAndSectionTypeNotIn(profileId, month, year, sectionTypes);
    	}
    }

	public List<ExpenseTracker> filterExpenses(ExpenseFilterRequestDTO expenseFilterRequestDTO) {
		return expenseTrackerRepositoryService.filterExpenses(expenseFilterRequestDTO);
	}
}

