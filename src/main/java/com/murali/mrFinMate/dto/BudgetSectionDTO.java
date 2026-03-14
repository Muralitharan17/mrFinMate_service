package com.murali.mrFinMate.dto;

import java.math.BigDecimal;

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
public class BudgetSectionDTO {
    private Long id;
    private String name;
    private BigDecimal percentage;
    private BigDecimal allocatedAmount;
    private BigDecimal spentAmount;
    private BigDecimal balanceAmount;
    
    public BudgetSectionDTO(String name, BigDecimal percentage, BigDecimal allocatedAmount, BigDecimal spentAmount,
			BigDecimal balanceAmount) {
    	this.id = System.nanoTime();
		this.name = name;
		this.percentage = percentage;
		this.allocatedAmount = allocatedAmount;
		this.spentAmount = spentAmount;
		this.balanceAmount = balanceAmount;
	}
    
    
}

