package com.murali.mrFinMate.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.murali.mrFinMate.dto.ProfileDTO;
import com.murali.mrFinMate.entity.Profile;
import com.murali.mrFinMate.repository.service.ProfileRepositoryService;
import com.murali.mrFinMate.utils.PopulateUtils;

@Service
public class ProfileControllerService {
	
	@Autowired
    private ProfileRepositoryService profileRepositoryService;
	
	@Autowired
	PopulateUtils populateUtils;
	
	// --- Profiles ---
    public List<ProfileDTO> getAllProfiles() {
    	List<ProfileDTO> profileDTOList = null;
    	List<Profile> profileList = profileRepositoryService.getAllProfiles();
    	if(profileList != null && !profileList.isEmpty()) {
    		profileDTOList = profileList.stream().map(entity -> {
    			ProfileDTO profileDTO = populateUtils.populateProfileDTOFromProfileEntity(entity);
				return profileDTO;
			}).toList();
    	}
        return profileDTOList;
    }

}
