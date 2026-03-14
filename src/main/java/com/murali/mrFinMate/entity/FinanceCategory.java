package com.murali.mrFinMate.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "finance_category")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class FinanceCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FINANCE_CATEGORY_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FINANCE_TYPE_ID", nullable = false)
    @ToString.Exclude
    @JsonBackReference // avoids recursion with FinanceType
    private FinanceType financeType;

    @Column(name = "FINANCE_CATEGORY_NAME", nullable = false)
    private String categoryName;

    @Column(name = "PERCENTAGE", precision = 5, scale = 2)
    private BigDecimal categoryPercentage;

    @Column(name = "ALLOTTED_AMOUNT", precision = 12, scale = 2)
    private BigDecimal allottedAmount;
    
    @Column(name = "SPENT_AMOUNT", precision = 12, scale = 2)
    private BigDecimal spentAmount = BigDecimal.ZERO;

    @Column(name = "BALANCE_AMOUNT", precision = 12, scale = 2, insertable = false, updatable = false)
    private BigDecimal balanceAmount = BigDecimal.ZERO;
    
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

    @OneToMany(mappedBy = "financeCategory", cascade = CascadeType.ALL, fetch = FetchType.LAZY,  orphanRemoval = true)
    @JsonManagedReference // manages detail children
    @ToString.Exclude
    private List<FinanceDetail> details;

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

