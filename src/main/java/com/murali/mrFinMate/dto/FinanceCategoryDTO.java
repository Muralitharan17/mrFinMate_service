package com.murali.mrFinMate.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class FinanceCategoryDTO {
    private Long id;
    private String typeName;
    private String name;
    private BigDecimal percentage;
    private BigDecimal allottedAmount;
    private BigDecimal spentAmount;
    private BigDecimal balanceAmount;
    private List<FinanceDetailDTO> details;
    
	public FinanceCategoryDTO(String typeName, String name, BigDecimal percentage, BigDecimal allottedAmount, BigDecimal spentAmount,
			BigDecimal balanceAmount) {
		this.id = System.nanoTime();
		this.typeName = typeName;
		this.name = name;
		this.percentage = percentage;
		this.allottedAmount = allottedAmount;
		this.spentAmount = spentAmount;
		this.balanceAmount = balanceAmount;
	}
    
    
}
