package com.murali.mrFinMate.dto;

import java.math.BigDecimal;
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
public class FinanceTypeDTO {
    private Long id;
    private Long sectionId;
    private String name;
    private BigDecimal percentage;
    private BigDecimal allottedAmount;
    private BigDecimal spentAmount;
    private BigDecimal balanceAmount;
    private List<FinanceCategoryDTO> categories;
    
	public FinanceTypeDTO(String name, BigDecimal percentage, BigDecimal allottedAmount, BigDecimal spentAmount,
			BigDecimal balanceAmount) {
		super();
		this.id = System.nanoTime();
		this.sectionId = System.nanoTime();
		this.name = name;
		this.percentage = percentage;
		this.allottedAmount = allottedAmount;
		this.spentAmount = spentAmount;
		this.balanceAmount = balanceAmount;
	}
    
    
}
