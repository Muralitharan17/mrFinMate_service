package com.murali.mrFinMate.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class FinanceDetailDTO {
    private Long id;
    private String name;
    private BigDecimal percentage;
    private BigDecimal allottedAmount;
}

