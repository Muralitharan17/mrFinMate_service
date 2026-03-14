package com.murali.mrFinMate.repository.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.murali.mrFinMate.entity.Profile;
import com.murali.mrFinMate.repository.ProfileRepository;

@Service
public class ProfileRepositoryService {
	
	@Autowired
    private ProfileRepository profileRepository;
	
	// --- Profiles ---
    public List<Profile> getAllProfiles() {
        return profileRepository.findAll();
    }
    
    public List<Profile> getAllActiveProfiles() {
        return profileRepository.findByDeletedDateIsNull();
    }
    
    
    public Profile findByProfileId(Long profileId) {
    	Optional<Profile> optional = profileRepository.findById(profileId);
		return optional.orElse(null);
	}
    
    public Profile saveOrUpdateProfile(Profile profile) {
        return profileRepository.save(profile);
    }

}
