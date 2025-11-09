package com.murali.mrFinMate.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.murali.mrFinMate.dto.FinanceRequest;
import com.murali.mrFinMate.dto.FinanceTypeDTO;
import com.murali.mrFinMate.service.FinanceTypeControllerService;

@RestController
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS})
public class FinanceTypeConstoller {
	
	@Autowired
	FinanceTypeControllerService financeTypeControllerService;
	
	@GetMapping("/fetchFinanceTypeConfig")
    public ResponseEntity<List<FinanceTypeDTO>> getFinanceTypeDetailsBySectionId(@RequestParam Long sectionId) {
        List<FinanceTypeDTO> response = financeTypeControllerService.getFinanceTypeDetailsBySectionId(sectionId);
        return ResponseEntity.ok(response);
    }

	
    @PostMapping("/saveOrUpdateFinanceTypeConfig")
    public ResponseEntity<List<FinanceTypeDTO>> saveOrUpdateFinanceTypeConfig(@RequestBody FinanceRequest financeRequest) {
    	financeTypeControllerService.saveOrUpdateFinanceTypeConfig(financeRequest);
    	List<FinanceTypeDTO> response =  financeTypeControllerService.getFinanceTypeDetailsBySectionId(financeRequest.getSectionId());
    	return ResponseEntity.ok(response);
    }

}
