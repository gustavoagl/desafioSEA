package com.sea.desafioseacorporation.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CepResponse {

    private String cep;

    @JsonProperty("logradouro")
    private String street;

    @JsonProperty("bairro")
    private String neighborhood;

    @JsonProperty("localidade")
    private String city;

    @JsonProperty("uf")
    private String state;

    private Boolean erro;
}