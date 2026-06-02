package com.sea.desafioseacorporation.dto.request;

import com.sea.desafioseacorporation.entity.Decision;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DecisionRequest {

    @NotNull(message = "Decisão obrigatória")
    private Decision decision;

    @NotBlank(message = "Comentário obrigatório")
    @Size(min = 10, max = 1000, message = "Comentário deve ter entre 10 e 1000 caracteres")
    private String comment;
}