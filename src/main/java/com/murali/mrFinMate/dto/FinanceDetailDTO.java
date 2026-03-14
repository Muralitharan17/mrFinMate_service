package com.murali.mrFinMate.dto;

import java.math.BigDecimal;
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
public class FinanceDetailDTO {
    private Long id;
    private String typeName;
    private String categoryName;
    private String name;
    private BigDecimal percentage;
    private BigDecimal allottedAmount;
    private BigDecimal spentAmount;
    private BigDecimal balanceAmount;
    
	public FinanceDetailDTO(String typeName, String categoryName, String name, BigDecimal percentage,
			BigDecimal allottedAmount, BigDecimal spentAmount, BigDecimal balanceAmount) {
		super();
		this.id = System.nanoTime();
		this.typeName = typeName;
		this.categoryName = categoryName;
		this.name = name;
		this.percentage = percentage;
		this.allottedAmount = allottedAmount;
		this.spentAmount = spentAmount;
		this.balanceAmount = balanceAmount;
	}
    
    
    
}

