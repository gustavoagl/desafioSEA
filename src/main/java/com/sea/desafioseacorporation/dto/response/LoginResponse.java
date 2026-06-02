package com.sea.desafioseacorporation.dto.response;

import com.sea.desafioseacorporation.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponse {

    private String message;
    private Long id;
    private String name;
    private String email;
    private Role role;

}