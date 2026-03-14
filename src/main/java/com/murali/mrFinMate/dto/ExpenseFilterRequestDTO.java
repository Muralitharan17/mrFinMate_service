package com.murali.mrFinMate.dto;

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
public class ExpenseFilterRequestDTO {

	private Long profileId;
    private String month;
    private String year;
    private ExpenseFilterDTO filter;
}
