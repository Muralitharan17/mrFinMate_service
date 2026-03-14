package com.murali.mrFinMate.repository.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.murali.mrFinMate.entity.User;
import com.murali.mrFinMate.repository.UserRepository;

@Service
public class UserRepositoryService {

    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User findByUserId(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    public User findByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

    public User saveOrUpdateUser(User user) {
        return userRepository.save(user);
    }
}
