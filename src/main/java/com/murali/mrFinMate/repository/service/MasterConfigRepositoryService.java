package com.murali.mrFinMate.repository.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.murali.mrFinMate.entity.MasterConfig;
import com.murali.mrFinMate.repository.MasterConfigRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MasterConfigRepositoryService {

    private final MasterConfigRepository masterConfigRepository;

    public List<MasterConfig> getConfigsByProfile(Long profileId) {
        return masterConfigRepository.findByProfile_Id(profileId);
    }

    public Optional<MasterConfig> findExisting(Long profileId, String configName, String month, String year) {
        return masterConfigRepository.findByProfile_IdAndConfigNameAndMonthAndYear(profileId, configName, month, year);
    }
   

    public List<MasterConfig> findByProfileIdAndConfigName(Long profileId, String configName) {
        return masterConfigRepository.findByProfile_IdAndConfigName(profileId, configName);
    }

    public MasterConfig save(MasterConfig config) {
        return masterConfigRepository.save(config);
    }

    public void delete(Long configId) {
        masterConfigRepository.deleteById(configId);
    }
}
