package com.sea.desafioseacorporation.dto.request;

import com.sea.desafioseacorporation.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateInternalUserRequest {

    @NotBlank(message = "Nome obrigatório")
    private String name;

    @Email(message = "Email inválido")
    @NotBlank(message = "Email obrigatório")
    private String email;

    @NotBlank(message = "Senha obrigatória")
    @Size(min = 6, message = "Senha precisa ter no mínimo 6 caracteres")
    private String password;

    @NotNull(message = "Role obrigatória")
    private Role role;
}
