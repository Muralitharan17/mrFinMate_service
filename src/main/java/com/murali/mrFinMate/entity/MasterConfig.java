package com.murali.mrFinMate.entity;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "master_config")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MasterConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CONFIG_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROFILE_ID", nullable = false)
    private Profile profile;

    @Column(name = "MONTH")
    private String month;

    @Column(name = "YEAR")
    private String year;

    @Column(name = "CONFIG_NAME", nullable = false, length = 100)
    private String configName;

    @Column(name = "CONFIG_VALUE", length = 250)
    private String configValue;

    @Column(name = "DESCRIPTION", length = 255)
    private String description;

    @Column(name = "ACTIVE")
    private Boolean active = true;

    @Column(name = "CREATED_USER", length = 100)
    private String createdUser;

    @Column(name = "UPDATED_USER", length = 100)
    private String updatedUser;

    @Column(name = "CREATED_DATE", nullable = false, updatable = false)
    private LocalDateTime createdDate = LocalDateTime.now();

    @Column(name = "UPDATED_DATE")
    private LocalDateTime updatedDate = LocalDateTime.now();

    @PreUpdate
    public void onUpdate() {
        this.updatedDate = LocalDateTime.now();
    }
}
