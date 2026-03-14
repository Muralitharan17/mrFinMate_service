package com.murali.mrFinMate.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.murali.mrFinMate.dto.UserDTO;
import com.murali.mrFinMate.entity.Profile;
import com.murali.mrFinMate.entity.User;
import com.murali.mrFinMate.repository.service.ProfileRepositoryService;
import com.murali.mrFinMate.repository.service.UserRepositoryService;

@Service
public class UserControllerService {

    @Autowired
    private UserRepositoryService userRepositoryService;
    
    @Autowired
    ProfileRepositoryService profileRepositoryService;

    // --- Get All Users ---
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepositoryService.getAllUsers();
        return users.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // --- Register User ---
    public ResponseEntity<Map<String, Object>> registerUser(UserDTO userDTO) {
        Map<String, Object> response = new HashMap<>();

        try {
            User existingUser = userRepositoryService.findByUserName(userDTO.getUserName());
            if (existingUser != null) {
                response.put("success", false);
                response.put("message", "Username already exists.");
                return ResponseEntity.ok(response);
            }

            User newUser = convertToEntity(userDTO);
            userRepositoryService.saveOrUpdateUser(newUser);
            
            // Creating Profile by default
            Profile profile = new Profile();
            
            profile.setName(userDTO.getUserName());
            profile.setIsManager(true);
            profile.setCreatedUser(userDTO.getUserName());
            profile.setUpdatedUser(userDTO.getUserName());
            profile.setCreatedDate(java.time.LocalDateTime.now());
            profile.setUpdatedDate(java.time.LocalDateTime.now());
            
            profileRepositoryService.saveOrUpdateProfile(profile);

            response.put("success", true);
            response.put("message", "User registered successfully.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error while registering user: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    // --- Login ---
    public ResponseEntity<Map<String, Object>> loginUser(Map<String, String> loginData) {
        Map<String, Object> response = new HashMap<>();
        String userName = loginData.get("userName");
        String password = loginData.get("password");

        try {
            User user = userRepositoryService.findByUserName(userName);
            if (user != null && user.getPassword().equals(password)) {
                response.put("success", true);
                response.put("message", "Login successful.");
                response.put("user", convertToDTO(user));
            } else {
                response.put("success", false);
                response.put("message", "Invalid username or password.");
            }
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error during login: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    // --- Update User ---
    public ResponseEntity<Map<String, Object>> updateUser(UserDTO userDTO) {
        Map<String, Object> response = new HashMap<>();

        try {
            User existingUser = userRepositoryService.findByUserId(userDTO.getId());
            if (existingUser == null) {
                response.put("success", false);
                response.put("message", "User not found.");
                return ResponseEntity.ok(response);
            }

            existingUser.setEmailId(userDTO.getEmailId());
            existingUser.setPhoneNo(userDTO.getPhoneNo());
            if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
                existingUser.setPassword(userDTO.getPassword());
            }

            userRepositoryService.saveOrUpdateUser(existingUser);

            response.put("success", true);
            response.put("message", "User updated successfully.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error updating user: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    // --- Get User By ID ---
    public ResponseEntity<UserDTO> getUserById(Long userId) {
        User user = userRepositoryService.findByUserId(userId);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(convertToDTO(user));
    }

    // --- Conversion Helpers ---
    private UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUserName(user.getUserName());
        dto.setPassword(user.getPassword());
        dto.setPhoneNo(user.getPhoneNo());
        dto.setEmailId(user.getEmailId());
        dto.setCreatedDate(user.getCreatedDate());
        dto.setUpdatedDate(user.getUpdatedDate());
        return dto;
    }

    private User convertToEntity(UserDTO dto) {
        User user = new User();
        user.setId(dto.getId());
        user.setUserName(dto.getUserName());
        user.setPassword(dto.getPassword());
        user.setPhoneNo(dto.getPhoneNo());
        user.setEmailId(dto.getEmailId());
        return user;
    }
}
