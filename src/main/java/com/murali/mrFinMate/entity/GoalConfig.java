package com.murali.mrFinMate.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
    name = "goal_config"
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GoalConfig {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
	
	@Column(name = "profile_id", nullable = false)
    private Long profileId;
	
	@Column(name = "goal_name", nullable = false)
    private String goalName;
	
	@Column(name = "is_active")
    private Boolean isActive = true;
	
	@Column(name = "target_amount", precision = 15, scale = 2, nullable = false)
    private BigDecimal targetAmount = BigDecimal.ZERO;

    @Column(name = "current_amount", precision = 15, scale = 2)
    private BigDecimal currentAmount = BigDecimal.ZERO;
    
 // DB-computed column; optional to map as read-only
    @Column(name = "required_amount", precision = 15, scale = 2, insertable = false, updatable = false)
    private BigDecimal requiredAmount;
    
    @Column(name = "created_user")
    private String createdUser;

    @Column(name = "updated_user")
    private String updatedUser;

    @Column(name = "created_date", insertable = false, updatable = false)
    private LocalDateTime createdDate;

    @Column(name = "updated_date", insertable = false, updatable = false)
    private LocalDateTime updatedDate;

    @Column(name = "remarks", length = 500)
    private String remarks;
    
    @PrePersist
    public void prePersist() {
        createdDate = LocalDateTime.now();
        updatedDate = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedDate = LocalDateTime.now();
    }

}
