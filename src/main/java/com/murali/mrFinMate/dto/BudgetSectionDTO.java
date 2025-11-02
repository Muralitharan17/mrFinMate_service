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
}

