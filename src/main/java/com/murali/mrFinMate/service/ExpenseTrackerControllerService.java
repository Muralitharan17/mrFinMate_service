package com.murali.mrFinMate.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class ExpenseTrackerControllerService {

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



    @Transactional
    public ExpenseTracker saveExpense(ExpenseTracker expense) {
        BigDecimal amount = expense.getAmount() == null ? BigDecimal.ZERO : expense.getAmount();

        // Save expense row first
        ExpenseTracker saved = expenseTrackerRepositoryService.save(expense);

        // Update budget_config.spent_salary & balance_salary
        BudgetConfig config = budgetConfigRepositoryService.findByProfileIdAndMonthAndYear(
            expense.getProfileId(), expense.getMonth(), expense.getYear());

        config.setSpentSalary(config.getSpentSalary().add(amount));
        // balanceSalary = budgetSalary - spentSalary
        config.setBalanceSalary(config.getBudgetSalary().subtract(config.getSpentSalary()));
        budgetConfigRepositoryService.save(config);

        // Find budget_section by section name
        BudgetSection section = budgetSectionRepositorySevice.findByBudgetConfigIdAndSectionName(config.getId(), expense.getSectionType());

        // Update section spent and balance
        section.setSpentAmount(section.getSpentAmount().add(amount));
        section.setBalanceAmount(section.getAllottedAmount().subtract(section.getSpentAmount()));
        budgetSectionRepositorySevice.save(section);

        // Now update finance_type -> finance_category -> finance_detail spent amounts
        // 1) FinanceType
        Optional<FinanceType> ftOpt = financeTypeRepositoryService
            .findByBudgetConfig_IdAndSectionIdAndFinanceTypeName(config.getId(), section.getId(), expense.getFinanceType());

        if (ftOpt.isPresent()) {
            FinanceType ft = ftOpt.get();
            ft.setSpentAmount(ft.getSpentAmount().add(amount));
            // Provided BALANCE is generated in DB as ALLOTTED - SPENT, otherwise compute:
            // ft.setBalanceAmount(ft.getAllottedAmount().subtract(ft.getSpentAmount()));
            financeTypeRepositoryService.save(ft);

            // 2) FinanceCategory
            if (expense.getFinanceCategory() != null) {
                Optional<FinanceCategory> fcOpt = financeCategoryRepositoryService
                    .findByFinanceType_IdAndFinanceCategoryName(ft.getId(), expense.getFinanceCategory());
                if (fcOpt.isPresent()) {
                    FinanceCategory fc = fcOpt.get();
                    fc.setSpentAmount(fc.getSpentAmount().add(amount));
                    // fc.setBalanceAmount(fc.getAllottedAmount().subtract(fc.getSpentAmount()));
                    financeCategoryRepositoryService.save(fc);

                    // 3) FinanceDetail
                    if (expense.getFinanceDetail() != null) {
                        Optional<FinanceDetail> fdOpt = financeDetailRepositoryService
                            .findByFinanceCategory_IdAndFinanceDetailName(fc.getId(), expense.getFinanceDetail());
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

        return saved;
    }

    @Transactional
    public ExpenseTracker updateExpense(Long id, ExpenseTracker updatedExpense) {
    	ExpenseTracker existing = expenseTrackerRepositoryService.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Expense not found"));

        BigDecimal oldAmount = existing.getAmount() == null ? BigDecimal.ZERO : existing.getAmount();
        BigDecimal newAmount = updatedExpense.getAmount() == null ? BigDecimal.ZERO : updatedExpense.getAmount();
        BigDecimal delta = newAmount.subtract(oldAmount); // positive -> additional spent, negative -> refund

        // update expense row
        existing.setDate(updatedExpense.getDate());
        existing.setTransactionType(updatedExpense.getTransactionType());
        existing.setSectionType(updatedExpense.getSectionType());
        existing.setFinanceType(updatedExpense.getFinanceType());
        existing.setFinanceCategory(updatedExpense.getFinanceCategory());
        existing.setFinanceDetail(updatedExpense.getFinanceDetail());
        existing.setAmount(newAmount);
        // update other fields...
        ExpenseTracker saved = expenseTrackerRepositoryService.save(existing);

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

        return saved;
    }

    @Transactional
    public void deleteExpense(Long id) {
        ExpenseTracker existing = expenseTrackerRepositoryService.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Expense not found"));

        BigDecimal amount = existing.getAmount() == null ? BigDecimal.ZERO : existing.getAmount();

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
    }

    public List<ExpenseTracker> getExpenses(Long profileId, String month, String year) {
        return expenseTrackerRepositoryService.findByProfileIdAndMonthAndYear(profileId, month, year);
    }
}

