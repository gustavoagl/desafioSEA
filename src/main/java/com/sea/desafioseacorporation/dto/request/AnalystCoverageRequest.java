package com.sea.desafioseacorporation.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AnalystCoverageRequest {

    @NotEmpty(message = "Informe pelo menos uma UF")
    private List<String> states;
}