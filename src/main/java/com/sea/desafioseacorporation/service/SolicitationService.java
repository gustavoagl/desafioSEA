package com.sea.desafioseacorporation.service;

import com.sea.desafioseacorporation.dto.request.Step1Request;
import com.sea.desafioseacorporation.dto.request.Step2Request;
import com.sea.desafioseacorporation.dto.request.Step3Request;
import com.sea.desafioseacorporation.dto.response.SolicitationResponse;
import com.sea.desafioseacorporation.dto.response.CepResponse;
import com.sea.desafioseacorporation.entity.*;
import com.sea.desafioseacorporation.repository.SolicitationRepository;
import com.sea.desafioseacorporation.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SolicitationService {

    private final SolicitationRepository solicitationRepository;
    private final UserRepository userRepository;
    private final CepService cepService;

    public SolicitationResponse createDraft(String userEmail) {
        UserEntity client = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        if (client.getRole() != Role.CLIENTE) {
            throw new IllegalArgumentException("Apenas CLIENTE pode criar solicitação");
        }

        Solicitation solicitation = new Solicitation();
        solicitation.setClientId(client.getId());
        solicitation.setStatus(Status.DRAFT);
        solicitation.setCurrentStep(1);

        Solicitation savedSolicitation = solicitationRepository.save(solicitation);

        return toResponse(savedSolicitation);
    }

    public SolicitationResponse updateStep1(Long solicitationId, String userEmail, Step1Request request) {
        UserEntity client = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        Solicitation solicitation = solicitationRepository.findById(solicitationId)
                .orElseThrow(() -> new IllegalArgumentException("Solicitação não encontrada"));

        validateOwner(solicitation, client);
        validateDraft(solicitation);

        solicitation.setServiceType(request.getServiceType());
        solicitation.setTitle(request.getTitle().trim());
        solicitation.setDescription(request.getDescription().trim());

        if (solicitation.getCurrentStep() == null || solicitation.getCurrentStep() < 1) {
            solicitation.setCurrentStep(1);
        }

        Solicitation savedSolicitation = solicitationRepository.save(solicitation);

        return toResponse(savedSolicitation);
    }

    public SolicitationResponse updateStep2(Long solicitationId, String userEmail, Step2Request request) {
        UserEntity client = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        Solicitation solicitation = solicitationRepository.findById(solicitationId)
                .orElseThrow(() -> new IllegalArgumentException("Solicitação não encontrada"));

        validateOwner(solicitation, client);
        validateDraft(solicitation);

        String normalizedCep = cepService.normalizeCep(request.getCep());
        CepResponse address = cepService.findAddressByCep(normalizedCep);

        solicitation.setCep(normalizedCep);
        solicitation.setNumber(request.getNumber().trim());
        solicitation.setComplement(
                request.getComplement() == null ? null : request.getComplement().trim()
        );
        solicitation.setStreet(address.getStreet().trim());
        solicitation.setNeighborhood(address.getNeighborhood().trim());
        solicitation.setCity(address.getCity().trim());
        solicitation.setState(address.getState().trim().toUpperCase());

        if (solicitation.getCurrentStep() == null || solicitation.getCurrentStep() < 2) {
            solicitation.setCurrentStep(2);
        }

        Solicitation savedSolicitation = solicitationRepository.save(solicitation);

        return toResponse(savedSolicitation);
    }

    public SolicitationResponse updateStep3(Long solicitationId, String userEmail, Step3Request request) {
        UserEntity client = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        Solicitation solicitation = solicitationRepository.findById(solicitationId)
                .orElseThrow(() -> new IllegalArgumentException("Solicitação não encontrada"));

        validateOwner(solicitation, client);
        validateDraft(solicitation);

        if (!Boolean.TRUE.equals(request.getTermsAccepted())) {
            throw new IllegalArgumentException("Os termos devem ser aceitos");
        }
//
//        if (request.getPriority() == Priority.HIGH){
//            throw new IllegalArgumentException("Para prioridade HIGH, o valor estimado deve ser maior ou igual a 100");
//        }

        solicitation.setPriority(request.getPriority());
        solicitation.setPreferredDate(request.getPreferredDate());
        solicitation.setTermsAccepted(request.getTermsAccepted());
        solicitation.setCurrentStep(3);

        Solicitation savedSolicitation = solicitationRepository.save(solicitation);

        return toResponse(savedSolicitation);
    }

    public SolicitationResponse submit(Long solicitationId, String userEmail) {
        UserEntity client = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        Solicitation solicitation = solicitationRepository.findById(solicitationId)
                .orElseThrow(() -> new IllegalArgumentException("Solicitação não encontrada"));

        validateOwner(solicitation, client);
        validateDraft(solicitation);
        validateCompleteForSubmit(solicitation);

        solicitation.setStatus(Status.SUBMMITED);
        solicitation.setSubmittedAt(LocalDateTime.now());

        Solicitation savedSolicitation = solicitationRepository.save(solicitation);

        return toResponse(savedSolicitation);
    }

    private void validateCompleteForSubmit(Solicitation solicitation) {
        validateStep1Complete(solicitation);
        validateStep2Complete(solicitation);
        validateStep3Complete(solicitation);
    }

    private void validateStep1Complete(Solicitation solicitation) {
        if (solicitation.getServiceType() == null) {
            throw new IllegalArgumentException("Step 1 incompleto: tipo de serviço obrigatório");
        }

        if (isBlank(solicitation.getTitle()) || solicitation.getTitle().trim().length() < 3 || solicitation.getTitle().trim().length() > 80) {
            throw new IllegalArgumentException("Step 1 incompleto: título deve ter entre 3 e 80 caracteres");
        }

        if (isBlank(solicitation.getDescription()) || solicitation.getDescription().trim().length() < 20 || solicitation.getDescription().trim().length() > 1000) {
            throw new IllegalArgumentException("Step 1 incompleto: descrição deve ter entre 20 e 1000 caracteres");
        }
    }

    private void validateStep2Complete(Solicitation solicitation) {
        if (isBlank(solicitation.getCep()) || !solicitation.getCep().matches("^\\d{8}$")) {
            throw new IllegalArgumentException("Step 2 incompleto: CEP inválido");
        }

        if (isBlank(solicitation.getNumber())) {
            throw new IllegalArgumentException("Step 2 incompleto: número obrigatório");
        }

        if (isBlank(solicitation.getStreet())) {
            throw new IllegalArgumentException("Step 2 incompleto: rua obrigatória");
        }

        if (isBlank(solicitation.getNeighborhood())) {
            throw new IllegalArgumentException("Step 2 incompleto: bairro obrigatório");
        }

        if (isBlank(solicitation.getCity())) {
            throw new IllegalArgumentException("Step 2 incompleto: cidade obrigatória");
        }

        if (isBlank(solicitation.getState()) || !solicitation.getState().matches("^[A-Z]{2}$")) {
            throw new IllegalArgumentException("Step 2 incompleto: UF inválida");
        }
    }

    private void validateStep3Complete(Solicitation solicitation) {
        if (solicitation.getPriority() == null) {
            throw new IllegalArgumentException("Step 3 incompleto: prioridade obrigatória");
        }

        if (solicitation.getPreferredDate() == null || solicitation.getPreferredDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Step 3 incompleto: data preferencial não pode estar no passado");
        }

        if (solicitation.getEstimatedValue() == null || solicitation.getEstimatedValue().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Step 3 incompleto: valor estimado deve ser maior ou igual a zero");
        }

        if (!Boolean.TRUE.equals(solicitation.getTermsAccepted())) {
            throw new IllegalArgumentException("Step 3 incompleto: termos devem ser aceitos");
        }

//        if (solicitation.getPriority() == Priority.HIGH
//                && solicitation.getEstimatedValue().compareTo(BigDecimal.valueOf(100)) < 0) {
//            throw new IllegalArgumentException("Step 3 inválido: prioridade HIGH exige valor estimado maior ou igual a 100");
//        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isBlank();
    }

    private void validateOwner(Solicitation solicitation, UserEntity client) {
        if (!solicitation.getClientId().equals(client.getId())) {
            throw new IllegalArgumentException("Você não pode alterar solicitação de outro cliente");
        }
    }

    private void validateDraft(Solicitation solicitation) {
        if (solicitation.getStatus() != Status.DRAFT) {
            throw new IllegalArgumentException("Só é possível editar solicitação em rascunho");
        }
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