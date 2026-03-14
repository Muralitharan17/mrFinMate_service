package com.murali.mrFinMate.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BudgetConfigDTO {
    private Long id;
    private Long profileId;
    private String month;
    private String year;
    private BigDecimal actualSalary;
    private BigDecimal spentSalary;
    private BigDecimal balanceSalary;
    private BigDecimal budgetPercentage;
    private BigDecimal budgetSalary;
    private String createdUser;
    private String updatedUser;
    private LocalDateTime deletedDate;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private List<BudgetSectionDTO> sections;
    
	public BudgetConfigDTO(Long profileId, String month, String year, BigDecimal actualSalary, BigDecimal spentSalary, BigDecimal balanceSalary,
			BigDecimal budgetSalary, BigDecimal budgetPercentage) {
		this.profileId = profileId;
		this.month = month;
		this.year = year;
		this.actualSalary = actualSalary;
		this.spentSalary = spentSalary;
		this.balanceSalary = balanceSalary;
		this.budgetSalary = budgetSalary;
		this.budgetPercentage = budgetPercentage;
	}
}

