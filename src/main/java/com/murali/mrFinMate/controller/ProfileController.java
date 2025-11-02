package com.murali.mrFinMate.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.murali.mrFinMate.dto.ProfileDTO;
import com.murali.mrFinMate.service.ProfileControllerService;

@RestController
@CrossOrigin(origins = "*")
public class ProfileController {
	
	@Autowired
	ProfileControllerService profileControllerService;
	
	@GetMapping("/profiles")
    public List<ProfileDTO> getProfiles() {
        return profileControllerService.getAllProfiles();
    }

}
