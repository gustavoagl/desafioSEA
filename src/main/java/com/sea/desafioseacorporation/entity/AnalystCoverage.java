package com.sea.desafioseacorporation.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(
        name = "analyst_coverage",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_analyst_coverage_user_state", columnNames = {"user_id", "state"})
        }
)
@Getter
@Setter
public class AnalystCoverage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "user_id")
    private Long userId;

    @Column(nullable = false, length = 2)
    private String state;
}