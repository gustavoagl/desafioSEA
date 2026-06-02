package com.sea.desafioseacorporation.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "solicitations")
@Getter
@Setter
public class Solicitation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "client_id")
    private Long clientId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Status status = Status.DRAFT;

    @Column(nullable = false, name = "current_step")
    private Integer currentStep = 1;

    @Column(nullable = false, name = "created_at")
    private LocalDateTime createdAt;

    @Column(nullable = false, name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "submitted_at")
    private LocalDateTime submittedAt;

    @Column(name = "analyzed_at")
    private LocalDateTime analyzedAt;

    @Column(name = "analyzed_by")
    private Long analyzedBy;

    @Column(name = "analysis_comment", length = 1000)
    private String analysisComment;

    @Enumerated(EnumType.STRING)
    @Column(name = "service_type", length = 30)
    private ServiceType serviceType;

    @Column(length = 80)
    private String title;

    @Column(length = 1000)
    private String description;

    @Column(length = 8)
    private String cep;

    @Column(length = 20)
    private String number;

    @Column(length = 100)
    private String complement;

    @Column(length = 150)
    private String street;

    @Column(length = 150)
    private String neighborhood;

    @Column(length = 150)
    private String city;

    @Column(length = 2)
    private String state;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Priority priority;

    @Column(name = "preferred_date")
    private LocalDate preferredDate;

    @Column(name = "estimated_value", precision = 12, scale = 2)
    private BigDecimal estimatedValue;

    @Column(name = "terms_accepted")
    private Boolean termsAccepted;

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();

        if (status == null) {
            status = Status.DRAFT;
        }

        if (currentStep == null) {
            currentStep = 1;
        }

        if (createdAt == null) {
            createdAt = now;
        }

        if (updatedAt == null) {
            updatedAt = now;
        }
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}