package com.murali.mrFinMate.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.murali.mrFinMate.dto.UserDTO;
import com.murali.mrFinMate.service.UserControllerService;

@RestController
@CrossOrigin(origins = "*")
public class UserController {
	
	@Autowired
    private UserControllerService userControllerService;

    @GetMapping("/getAllUsers")
    public List<UserDTO> getAllUsers() {
        return userControllerService.getAllUsers();
    }

    @PostMapping("/user/register")
    public ResponseEntity<Map<String, Object>> registerUser(@RequestBody UserDTO userDTO) {
        return userControllerService.registerUser(userDTO);
    }

    @PostMapping("/user/login")
    public ResponseEntity<Map<String, Object>> loginUser(@RequestBody Map<String, String> loginData) {
        return userControllerService.loginUser(loginData);
    }

    @PutMapping("/updateUser")
    public ResponseEntity<Map<String, Object>> updateUser(@RequestBody UserDTO userDTO) {
        return userControllerService.updateUser(userDTO);
    }

    @GetMapping("/getUserById/{userId}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long userId) {
        return userControllerService.getUserById(userId);
    }
}
