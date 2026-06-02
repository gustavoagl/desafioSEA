package com.sea.desafioseacorporation.dto.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {
    @NotBlank(message = "Nome obrigatorio")
    private String name;

    @Email(message = "Email incorreto")
    @NotBlank(message = "Email obrigatorio")
    private String email;

    @NotBlank(message = "Senha obrigatoria")
    @Size(min = 6,message = "Senha precisa ter no minimo 6 caracteres")
    private String password;
}
