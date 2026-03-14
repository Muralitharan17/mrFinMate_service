package com.murali.mrFinMate.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.murali.mrFinMate.dto.BudgetConfigDTO;
import com.murali.mrFinMate.service.InheritanceControllerService;

@RestController
@CrossOrigin(origins = "*") // allow React front-end
public class InheritanceController {
	
	@Autowired
    private InheritanceControllerService inheritanceControllerService;
	
	
	/**
     * Clone all configuration for a profile from sourceMonth/sourceYear -> targetMonth/targetYear
     * - All cloning happens server-side and saved to DB using new primary keys.
     * - Returns the newly created BudgetConfigDTO for the target month/year (so UI can refresh).
     */
	@PostMapping("/inheritConfigurations")
    public ResponseEntity<?> inheritConfigurations(
            @RequestParam Long profileId,
            @RequestParam String sourceMonth,
            @RequestParam String sourceYear,
            @RequestParam String targetMonth,
            @RequestParam String targetYear) {

        try {
            BudgetConfigDTO created = inheritanceControllerService.inherit(profileId, sourceMonth, sourceYear, targetMonth, targetYear);
            return ResponseEntity.ok(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(Map.of("success", false, "message", "Error during inheritance"));
        }
    }

}
