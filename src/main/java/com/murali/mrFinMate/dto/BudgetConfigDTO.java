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
    private BigDecimal budgetPercentage;
    private BigDecimal budgetSalary;
    private String createdUser;
    private String updatedUser;
    private LocalDateTime deletedDate;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private List<BudgetSectionDTO> sections;
}

