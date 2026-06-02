package com.sea.desafioseacorporation.dto.request;

import com.sea.desafioseacorporation.entity.Priority;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class Step3Request {
    private Priority priority;

    private LocalDate preferredDate;

    private Long estimatedValue;

    private Boolean termsAccepted;
}
