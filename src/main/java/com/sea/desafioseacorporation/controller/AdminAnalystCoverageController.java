package com.sea.desafioseacorporation.controller;

import com.sea.desafioseacorporation.dto.request.AnalystCoverageRequest;
import com.sea.desafioseacorporation.dto.response.AnalystCoverageResponse;
import com.sea.desafioseacorporation.service.AnalystCoverageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/analistas")
@RequiredArgsConstructor
public class AdminAnalystCoverageController {

    private final AnalystCoverageService analystCoverageService;

    @PutMapping("/{id}/cover")
    public AnalystCoverageResponse updateCover(
            @PathVariable Long id,
            @Valid @RequestBody AnalystCoverageRequest request
    ) {
        return analystCoverageService.updateCoverage(id, request);
    }
}