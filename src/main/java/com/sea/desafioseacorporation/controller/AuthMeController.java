package com.sea.desafioseacorporation.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AuthMeController {

    @GetMapping("/auth/me")
    public MeResponse me(Authentication authentication) {
        List<String> roles = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return new MeResponse(
                authentication.getName(),
                roles
        );
    }

    public record MeResponse(
            String email,
            List<String> roles
    ) {
    }
}