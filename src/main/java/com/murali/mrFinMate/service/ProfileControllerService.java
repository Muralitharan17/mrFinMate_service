package com.murali.mrFinMate.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.murali.mrFinMate.dto.ProfileDTO;
import com.murali.mrFinMate.entity.Profile;
import com.murali.mrFinMate.entity.User;
import com.murali.mrFinMate.repository.service.ProfileRepositoryService;
import com.murali.mrFinMate.repository.service.UserRepositoryService;
import com.murali.mrFinMate.utils.PopulateUtils;

@Service
public class ProfileControllerService {
	
	@Autowired
    private ProfileRepositoryService profileRepositoryService;
	
	@Autowired
    private UserRepositoryService userRepositoryService;
	
	@Autowired
	PopulateUtils populateUtils;
	
	// --- Profiles ---
    public List<ProfileDTO> getAllActiveProfiles() {
    	List<ProfileDTO> profileDTOList = null;
    	List<Profile> profileList = profileRepositoryService.getAllActiveProfiles();
    	if(profileList != null && !profileList.isEmpty()) {
    		profileDTOList = profileList.stream().map(entity -> {
    			ProfileDTO profileDTO = populateUtils.populateProfileDTOFromProfileEntity(entity);
				return profileDTO;
			}).toList();
    	}
        return profileDTOList;
    }
    
    public ProfileDTO saveOrUpdateProfile(ProfileDTO profileDTO) {
        try {
            Profile profileEntity = null;

            if (profileDTO.getId() != null) {
                profileEntity = profileRepositoryService.findByProfileId(profileDTO.getId());
            }

            if (profileEntity == null) {
                profileEntity = new Profile();
            }

            profileEntity.setName(profileDTO.getName());
            profileEntity.setIsManager(profileDTO.getIsManager() != null ? profileDTO.getIsManager() : false);
            profileEntity.setCreatedUser(profileDTO.getCreatedUser());
            profileEntity.setUpdatedUser(profileDTO.getUpdatedUser());

            if (profileDTO.getUserId() != null) {
                User user = userRepositoryService.findByUserId(profileDTO.getUserId());
                profileEntity.setUser(user);
            }

            Profile savedProfile = profileRepositoryService.saveOrUpdateProfile(profileEntity);
            return populateUtils.populateProfileDTOFromProfileEntity(savedProfile);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public boolean deleteProfile(Long id) {
        try {
            Profile profile = profileRepositoryService.findByProfileId(id);
            if (profile != null) {
                profile.setDeletedDate(java.time.LocalDateTime.now());
                profileRepositoryService.saveOrUpdateProfile(profile);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
