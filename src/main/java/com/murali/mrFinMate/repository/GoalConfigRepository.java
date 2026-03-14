package com.murali.mrFinMate.repository;

import com.murali.mrFinMate.entity.GoalConfig;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GoalConfigRepository extends JpaRepository<GoalConfig, Integer> {

    List<GoalConfig> findByProfileIdAndIsActiveOrderByTargetAmountAsc(Long profileId, Boolean isActive);

    GoalConfig findByProfileIdAndGoalName(Long profileId, String goalName);

}
