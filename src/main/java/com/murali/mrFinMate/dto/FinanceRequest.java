package com.murali.mrFinMate.dto;

import java.util.List;

import lombok.Data;

@Data
public class FinanceRequest {
    private Long profileId;
    private String month;
    private String year;
    private Long sectionId;
    private List<FinanceTypeDTO> financeTypes;
}
