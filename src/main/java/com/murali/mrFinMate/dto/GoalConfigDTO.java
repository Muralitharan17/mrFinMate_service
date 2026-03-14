package com.murali.mrFinMate.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
public class GoalConfigDTO {

	private Integer id;
	private Long profileId;
	private String goalName;
	private Boolean isActive;
	private BigDecimal targetAmount;
	private BigDecimal currentAmount;
	private BigDecimal requiredAmount;
	private String remarks;
	private String createdUser;
	private String updatedUser;
	private LocalDateTime createdDate;
	private LocalDateTime updatedDate;
}
