package com.sea.desafioseacorporation.controller;

import com.sea.desafioseacorporation.dto.request.Step1Request;
import com.sea.desafioseacorporation.dto.request.Step2Request;
import com.sea.desafioseacorporation.dto.request.Step3Request;
import com.sea.desafioseacorporation.dto.response.SolicitationResponse;
import com.sea.desafioseacorporation.service.SolicitationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/solicitations")
@RequiredArgsConstructor
public class SolicitationController {

    private final SolicitationService solicitationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SolicitationResponse createDraft(Authentication authentication) {
        return solicitationService.createDraft(authentication.getName());
    }

    @PatchMapping("/{id}/step-1")
    public SolicitationResponse updateStep1(
            @PathVariable Long id,
            @Valid @RequestBody Step1Request request,
            Authentication authentication
    ) {
        return solicitationService.updateStep1(id, authentication.getName(), request);
    }
    @PatchMapping("/{id}/step-2")
    public SolicitationResponse updateStep2(
            @PathVariable Long id,
            @Valid @RequestBody Step2Request request,
            Authentication authentication
    ) {
        return solicitationService.updateStep2(id, authentication.getName(), request);
    }
    @PatchMapping("/{id}/step-3")
    public SolicitationResponse updateStep3(
            @PathVariable Long id,
            @Valid @RequestBody Step3Request request,
            Authentication authentication
    ) {
        return solicitationService.updateStep3(id, authentication.getName(), request);
    }
    @PostMapping("/{id}/submit")
    public SolicitationResponse submit(
            @PathVariable Long id,
            Authentication authentication
    ) {
        return solicitationService.submit(id, authentication.getName());
    }
}