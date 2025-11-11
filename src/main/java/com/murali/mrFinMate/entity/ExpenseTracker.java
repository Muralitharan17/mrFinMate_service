package com.murali.mrFinMate.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
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
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "expense_tracker")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ExpenseTracker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EXPENSE_ID")
    private Long id;

    @Column(name = "PROFILE_ID", nullable = false)
    private Long profileId;

    @Column(name = "MONTH", nullable = false)
    private String month;

    @Column(name = "YEAR", nullable = false)
    private String year;

    @Column(name = "DATE", nullable = false)
    private LocalDate date;

    @Column(name = "TRANSACTION_TYPE", nullable = false)
    private String transactionType;

    @Column(name = "SECTION_TYPE", nullable = false)
    private String sectionType;

    @Column(name = "FINANCE_TYPE", nullable = false)
    private String financeType;

    @Column(name = "CATEGORY", nullable = false)
    private String financeCategory;

    @Column(name = "DETAIL", nullable = false)
    private String financeDetail;

    @Column(name = "AMOUNT", nullable = false)
    private BigDecimal amount = BigDecimal.ZERO;

    @Column(name = "BALANCE", nullable = false)
    private BigDecimal balance = BigDecimal.ZERO;

    @Column(name = "CREATED_USER")
    private String createdUser;

    @Column(name = "UPDATED_USER")
    private String updatedUser;

    @Column(name = "CREATED_DATE", nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @Column(name = "UPDATED_DATE", nullable = false)
    private LocalDateTime updatedDate;

    @PrePersist
    protected void onCreate() {
        createdDate = LocalDateTime.now();
        updatedDate = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedDate = LocalDateTime.now();
    }

    // Getters and setters omitted for brevity (keep the same structure as existing entities)
}
