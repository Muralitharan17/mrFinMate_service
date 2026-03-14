package com.murali.mrFinMate.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
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
        return profileControllerService.getAllActiveProfiles();
    }

    @PostMapping("/saveOrUpdateProfile")
    public ProfileDTO saveOrUpdateProfile(@RequestBody ProfileDTO profileDTO) {
        return profileControllerService.saveOrUpdateProfile(profileDTO);
    }

    @DeleteMapping("/deleteProfile")
    public boolean deleteProfile(@RequestParam Long profileId) {
        return profileControllerService.deleteProfile(profileId);
    }
}
