package com.sea.desafioseacorporation.service;

import com.sea.desafioseacorporation.dto.response.CepResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class CepService {
    private final RestClient.Builder restClientBuilder;

    public CepResponse findAddressByCep(String cep) {
        String normalizedCep = normalizeCep(cep);

        try {
            CepResponse response = restClientBuilder.build()
                    .get()
                    .uri("https://viacep.com.br/ws/{cep}/json/", normalizedCep)
                    .retrieve()
                    .body(CepResponse.class);

            if (response == null || Boolean.TRUE.equals(response.getErro())) {
                throw new IllegalArgumentException("CEP inválido");
            }

            validateAddress(response);

            return response;
        } catch (IllegalArgumentException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new IllegalArgumentException("Não foi possível consultar o CEP");
        }
    }

    public String normalizeCep(String cep) {
        if (cep == null) {
            throw new IllegalArgumentException("CEP obrigatório");
        }

        String normalizedCep = cep.replaceAll("\\D", "");

        if (!normalizedCep.matches("^\\d{8}$")) {
            throw new IllegalArgumentException("CEP deve conter 8 dígitos");
        }

        return normalizedCep;
    }

    private void validateAddress(CepResponse response) {
        if (isBlank(response.getStreet())) {
            throw new IllegalArgumentException("Rua não encontrada para o CEP informado");
        }

        if (isBlank(response.getNeighborhood())) {
            throw new IllegalArgumentException("Bairro não encontrado para o CEP informado");
        }

        if (isBlank(response.getCity())) {
            throw new IllegalArgumentException("Cidade não encontrada para o CEP informado");
        }

        if (isBlank(response.getState()) || !response.getState().matches("^[A-Z]{2}$")) {
            throw new IllegalArgumentException("UF inválida para o CEP informado");
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isBlank();
    }
}