package com.murali.mrFinMate.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

@Data
public class FinanceTypeDTO {
    private Long id;
    private Long sectionId;
    private String name;
    private BigDecimal percentage;
    private BigDecimal allottedAmount;
    private BigDecimal spentAmount;
    private BigDecimal balanceAmount;
    private List<FinanceCategoryDTO> categories;
}
