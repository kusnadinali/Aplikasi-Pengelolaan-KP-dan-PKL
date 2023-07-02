package com.jtk.ps.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.jtk.ps.api.model.Evaluation;

@Repository
public interface EvaluationRepository extends JpaRepository<Evaluation, Integer>{
    List<Evaluation> findByProdiId(Integer prodiId);

    List<Evaluation> findByParticipantId(Integer participantId);

    @Query(value = "select * from evaluation order by update_date desc limit 10", nativeQuery = true)
    List<Evaluation> findAllByUpdateDate();
}
