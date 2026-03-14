package com.murali.mrFinMate.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.murali.mrFinMate.entity.Profile;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
    Profile findByName(String name);
    
    List<Profile> findByDeletedDateIsNull();
}

