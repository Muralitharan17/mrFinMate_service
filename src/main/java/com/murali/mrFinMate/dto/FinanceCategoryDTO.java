package com.murali.mrFinMate.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

@Data
public class FinanceCategoryDTO {
    private Long id;
    private String name;
    private BigDecimal percentage;
    private BigDecimal allottedAmount;
    private BigDecimal spentAmount;
    private BigDecimal balanceAmount;
    private List<FinanceDetailDTO> details;
}
