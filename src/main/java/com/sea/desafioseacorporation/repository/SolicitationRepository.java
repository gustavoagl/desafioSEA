package com.sea.desafioseacorporation.repository;

import com.sea.desafioseacorporation.entity.Solicitation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SolicitationRepository extends JpaRepository<Solicitation, Long> {

    List<Solicitation> findByClientId(Long clientId);
}