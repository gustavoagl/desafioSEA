package com.sea.desafioseacorporation.repository;

import com.sea.desafioseacorporation.entity.AnalystCoverage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AnalystCoverageRepository extends JpaRepository<AnalystCoverage, Long> {

    List<AnalystCoverage> findByUserId(Long userId);
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("delete from AnalystCoverage ac where ac.userId = :userId")
    void deleteByUserId(Long userId);

    boolean existsByUserIdAndState(Long userId, String state);
}