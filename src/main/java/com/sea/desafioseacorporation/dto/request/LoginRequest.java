package com.sea.desafioseacorporation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {

    @NotBlank(message = "email invalido")
    private String email;

    @NotBlank(message = "Senha obrigatoria")
    @Size(min = 6,message = "Senha precisa ter no minimo 6 caracteres")
    private String password;

}
