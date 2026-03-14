package com.murali.mrFinMate.dto;

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
public class ExpenseFilterDTO {

	private String singleDate;
    private String fromDate;
    private String toDate;

    private List<String> transactionTypes;
    private List<String> sectionTypes;
    private List<String> financeTypes;
    private List<String> categories;
    private List<String> details;

    private Double minAmount;
    private Double maxAmount;
    
    private String remark;
   
}
