package com.sea.desafioseacorporation.service;

import com.sea.desafioseacorporation.dto.request.AnalystCoverageRequest;
import com.sea.desafioseacorporation.dto.response.AnalystCoverageResponse;
import com.sea.desafioseacorporation.entity.AnalystCoverage;
import com.sea.desafioseacorporation.entity.Role;
import com.sea.desafioseacorporation.entity.UserEntity;
import com.sea.desafioseacorporation.repository.AnalystCoverageRepository;
import com.sea.desafioseacorporation.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnalystCoverageService {

    private final AnalystCoverageRepository analystCoverageRepository;
    private final UserRepository userRepository;

    @Transactional
    public AnalystCoverageResponse updateCoverage(Long analystId, AnalystCoverageRequest request) {
        UserEntity analyst = userRepository.findById(analystId)
                .orElseThrow(() -> new IllegalArgumentException("Analista não encontrado"));

        if (analyst.getRole() != Role.ANALISTA) {
            throw new IllegalArgumentException("Usuário informado não é ANALYST");
        }

        List<String> normalizedStates = request.getStates()
                .stream()
                .map(this::normalizeState)
                .distinct()
                .toList();

        analystCoverageRepository.deleteByUserId(analystId);

        List<AnalystCoverage> coverages = normalizedStates.stream()
                .map(state -> {
                    AnalystCoverage coverage = new AnalystCoverage();
                    coverage.setUserId(analystId);
                    coverage.setState(state);
                    return coverage;
                })
                .toList();

        analystCoverageRepository.saveAll(coverages);

        return new AnalystCoverageResponse(analystId, normalizedStates);
    }

    private String normalizeState(String uf) {
        if (uf == null) {
            throw new IllegalArgumentException("UF não pode ser nula");
        }

        String normalized = uf.trim().toUpperCase();

        if (!normalized.matches("^[A-Z]{2}$")) {
            throw new IllegalArgumentException("UF inválida: " + uf);
        }

        return normalized;
    }
}
