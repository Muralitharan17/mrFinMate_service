package com.murali.mrFinMate.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.murali.mrFinMate.dto.MasterConfigDTO;
import com.murali.mrFinMate.service.MasterConfigControllerService;

import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class MasterConfigController {

    private final MasterConfigControllerService masterConfigControllerService;

    @GetMapping("/fetchMasterConfig")
    public ResponseEntity<List<MasterConfigDTO>> getMasterConfig(@RequestParam Long profileId) {
        List<MasterConfigDTO> configs = masterConfigControllerService.fetchMasterConfig(profileId);
        return ResponseEntity.ok(configs);
    }

    @PostMapping("/saveOrUpdateMasterConfig")
    public ResponseEntity<MasterConfigDTO> saveOrUpdateMasterConfig(@RequestBody MasterConfigDTO dto) {
        MasterConfigDTO saved = masterConfigControllerService.saveOrUpdateMasterConfig(dto);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/deleteMasterConfig")
    public ResponseEntity<Void> deleteMasterConfig(@RequestParam Long configId) {
        masterConfigControllerService.deleteMasterConfig(configId);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/value")
    public String getConfigValue(
            @RequestParam Long profileId,
            @RequestParam(required = false) String month,
            @RequestParam(required = false) String year,
            @RequestParam String configName) {

        return masterConfigControllerService.getConfigValue(profileId, month, year, configName);
    }
}
