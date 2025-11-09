package com.murali.mrFinMate.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.murali.mrFinMate.entity.MasterConfig;

@Repository
public interface MasterConfigRepository extends JpaRepository<MasterConfig, Long> {

    List<MasterConfig> findByProfile_Id(Long profileId);

    Optional<MasterConfig> findByProfile_IdAndConfigNameAndMonthAndYear(
            Long profileId, String configName, String month, String year);

    List<MasterConfig> findByProfile_IdAndConfigName(Long profileId, String configName);
}
