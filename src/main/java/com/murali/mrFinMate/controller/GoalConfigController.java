package com.murali.mrFinMate.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.murali.mrFinMate.entity.GoalConfig;
import com.murali.mrFinMate.service.GoalConfigControllerService;

@RestController
@CrossOrigin(origins = "*") // allow React front-end
public class GoalConfigController {
	
	@Autowired
	GoalConfigControllerService goalConfigControllerService;
	
	@GetMapping("/goalConfig/{profileId}")
    public List<GoalConfig> getGoals(@PathVariable Long profileId) {
        return goalConfigControllerService.getGoals(profileId);
    }

    @PostMapping("/goalConfig/save")
    public GoalConfig saveGoal(@RequestBody GoalConfig goal) {
        return goalConfigControllerService.saveGoal(goal);
    }

    @DeleteMapping("/goalConfig/{id}")
    public void deleteGoal(@PathVariable Integer id) {
    	goalConfigControllerService.deleteGoal(id);
    }

}
