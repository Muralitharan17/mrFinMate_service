package com.murali.mrFinMate.repository.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.murali.mrFinMate.entity.GoalConfig;
import com.murali.mrFinMate.repository.GoalConfigRepository;

@Service
public class GoalConfigRepositoryService {
	
	@Autowired
	GoalConfigRepository goalConfigRepository;

	public List<GoalConfig> findByProfileIdAndIsActive(Long profileId) {
		return goalConfigRepository.findByProfileIdAndIsActiveOrderByTargetAmountAsc(profileId, true);
	}

	public GoalConfig save(GoalConfig goal) {
		return goalConfigRepository.save(goal);
	}

	public void deleteById(Integer id) {
		 goalConfigRepository.deleteById(id);
	}

	public GoalConfig findByProfileIdAndGoalName(Long profileId, String name) {
		return goalConfigRepository.findByProfileIdAndGoalName(profileId, name);
	}

	

}
