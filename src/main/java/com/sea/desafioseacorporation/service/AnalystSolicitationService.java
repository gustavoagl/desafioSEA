package com.sea.desafioseacorporation.service;

import com.sea.desafioseacorporation.dto.request.DecisionRequest;
import com.sea.desafioseacorporation.dto.response.SolicitationResponse;
import com.sea.desafioseacorporation.entity.AnalystCoverage;
import com.sea.desafioseacorporation.entity.Decision;
import com.sea.desafioseacorporation.entity.Role;
import com.sea.desafioseacorporation.entity.Solicitation;
import com.sea.desafioseacorporation.entity.Status;
import com.sea.desafioseacorporation.entity.UserEntity;
import com.sea.desafioseacorporation.repository.AnalystCoverageRepository;
import com.sea.desafioseacorporation.repository.SolicitationRepository;
import com.sea.desafioseacorporation.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnalystSolicitationService {

    private final SolicitationRepository solicitationRepository;
    private final UserRepository userRepository;
    private final AnalystCoverageRepository analystCoverageRepository;

    public SolicitationResponse getById(Long solicitationId, String userEmail) {
        UserEntity user = findUser(userEmail);
        Solicitation solicitation = findSolicitation(solicitationId);

        validateAnalystAccess(user, solicitation);

        return toResponse(solicitation);
    }

    public SolicitationResponse startReview(Long solicitationId, String userEmail) {
        UserEntity user = findUser(userEmail);
        Solicitation solicitation = findSolicitation(solicitationId);

        validateAnalystAccess(user, solicitation);

        if (solicitation.getStatus() != Status.SUBMMITED) {
            throw new IllegalArgumentException("Só é possível iniciar análise de solicitação SUBMITTED");
        }

        solicitation.setStatus(Status.IN_REVIEW);

        Solicitation savedSolicitation = solicitationRepository.save(solicitation);

        return toResponse(savedSolicitation);
    }

    public SolicitationResponse decide(Long solicitationId, String userEmail, DecisionRequest request) {
        UserEntity user = findUser(userEmail);
        Solicitation solicitation = findSolicitation(solicitationId);

        validateAnalystAccess(user, solicitation);

        if (solicitation.getStatus() != Status.SUBMMITED && solicitation.getStatus() != Status.IN_REVIEW) {
            throw new IllegalArgumentException("Só é possível decidir solicitação SUBMITTED ou IN_REVIEW");
        }

        if (request.getDecision() == Decision.APROVADO) {
            solicitation.setStatus(Status.APROVED);
        } else if (request.getDecision() == Decision.RECUSADO) {
            solicitation.setStatus(Status.REJECTED);
        } else {
            throw new IllegalArgumentException("Decisão inválida");
        }

        solicitation.setAnalysisComment(request.getComment().trim());
        solicitation.setAnalyzedBy(user.getId());
        solicitation.setAnalyzedAt(LocalDateTime.now());

        Solicitation savedSolicitation = solicitationRepository.save(solicitation);

        return toResponse(savedSolicitation);
    }

    private void validateAnalystAccess(UserEntity user, Solicitation solicitation) {
        if (user.getRole() == Role.ADMIN) {
            return;
        }

        if (user.getRole() != Role.ANALISTA) {
            throw new IllegalArgumentException("Apenas ANALYST ou ADMIN pode acessar análise");
        }

        if (solicitation.getState() == null || solicitation.getState().isBlank()) {
            throw new IllegalArgumentException("Solicitação sem UF definida");
        }

        List<String> allowedStates = analystCoverageRepository.findByUserId(user.getId())
                .stream()
                .map(AnalystCoverage::getState)
                .toList();

        if (!allowedStates.contains(solicitation.getState())) {
            throw new IllegalArgumentException("Analista não possui cobertura para a UF da solicitação");
        }
    }

    private UserEntity findUser(String userEmail) {
        return userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
    }

    private Solicitation findSolicitation(Long solicitationId) {
        return solicitationRepository.findById(solicitationId)
                .orElseThrow(() -> new IllegalArgumentException("Solicitação não encontrada"));
    }

    private SolicitationResponse toResponse(Solicitation solicitation) {
        return new SolicitationResponse(
                solicitation.getId(),
                solicitation.getClientId(),
                solicitation.getStatus(),
                solicitation.getCurrentStep(),
                solicitation.getServiceType(),
                solicitation.getTitle(),
                solicitation.getDescription(),
                solicitation.getCep(),
                solicitation.getNumber(),
                solicitation.getComplement(),
                solicitation.getStreet(),
                solicitation.getNeighborhood(),
                solicitation.getCity(),
                solicitation.getState(),
                solicitation.getPriority(),
                solicitation.getPreferredDate(),
                solicitation.getEstimatedValue(),
                solicitation.getTermsAccepted(),
                solicitation.getCreatedAt(),
                solicitation.getUpdatedAt(),
                solicitation.getSubmittedAt()
        );
    }
}