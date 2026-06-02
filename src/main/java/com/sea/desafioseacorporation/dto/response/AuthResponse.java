package com.sea.desafioseacorporation.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthResponse {

    private String message;
    private String token;
    private String type;
}