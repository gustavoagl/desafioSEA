package com.sea.desafioseacorporation.controller;

import com.sea.desafioseacorporation.dto.request.CreateInternalUserRequest;
import com.sea.desafioseacorporation.dto.response.RegisterResponse;
import com.sea.desafioseacorporation.service.AdminUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final AdminUserService adminUserService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RegisterResponse createInternalUser(@Valid @RequestBody CreateInternalUserRequest request) {
        return adminUserService.createInternalUser(request);
    }
}