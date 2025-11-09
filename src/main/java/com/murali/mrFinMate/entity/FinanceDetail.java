package com.murali.mrFinMate.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "finance_detail")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class FinanceDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FINANCE_DETAIL_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FINANCE_CATEGORY_ID", nullable = false)
    @ToString.Exclude
    @JsonBackReference // avoids recursion with FinanceCategory
    private FinanceCategory financeCategory;

    @Column(name = "FINANCE_DETAIL_NAME", nullable = false)
    private String detailName;

    @Column(name = "PERCENTAGE", precision = 5, scale = 2)
    private BigDecimal detailPercentage;

    @Column(name = "ALLOTTED_AMOUNT", precision = 12, scale = 2)
    private BigDecimal allottedAmount;
    
    @Column(name = "SPENT_AMOUNT", precision = 12, scale = 2)
    private BigDecimal spentAmount;

    @Column(name = "BALANCE_AMOUNT", precision = 12, scale = 2, insertable = false, updatable = false)
    private BigDecimal balanceAmount;
    
    @Column(name = "REMARKS", length = 255)
    private String remarks;

    @Column(name = "CREATED_USER")
    private String createdUser;

    @Column(name = "UPDATED_USER")
    private String updatedUser;

    @Column(name = "DELETED_DATE")
    private LocalDateTime deletedDate;

    @Column(name = "CREATED_DATE", updatable = false)
    private LocalDateTime createdDate;

    @Column(name = "UPDATED_DATE")
    private LocalDateTime updatedDate;

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
