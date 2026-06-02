package com.sea.desafioseacorporation.dto.response;

import com.sea.desafioseacorporation.entity.Priority;
import com.sea.desafioseacorporation.entity.ServiceType;
import com.sea.desafioseacorporation.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class SolicitationResponse {

    private Long id;
    private Long clientId;
    private Status status;
    private Integer currentStep;

    private ServiceType serviceType;
    private String title;
    private String description;

    private String cep;
    private String number;
    private String complement;
    private String street;
    private String neighborhood;
    private String city;
    private String state;

    private Priority priority;
    private LocalDate preferredDate;
    private BigDecimal estimatedValue;
    private Boolean termsAccepted;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime submittedAt;
}