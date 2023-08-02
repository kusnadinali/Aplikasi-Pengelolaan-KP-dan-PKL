package com.jtk.ps.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.jtk.ps.api.model.Participant;
import com.jtk.ps.api.model.Valuation;

@Repository
public interface ValuationRepository extends JpaRepository<Valuation, Integer>{

    Optional<Valuation> findByAspectNameAndEvaluation_ParticipantAndEvaluation_NumEvaluation(String aspectName, Participant participant, Integer numEvaluation);
    
    @Query(value = "select v.* from valuation v join evaluation e on e.id = v.evaluation_id where e.participant_id = :participantId and e.num_evaluation = :numEvaluation and v.aspect_name like :aspectName", nativeQuery = true)
    Optional<Valuation> findValueByParticipant(String aspectName, Integer numEvaluation, Integer participantId);

    @Query(value = "select * from valuation where evaluation_id = :id", nativeQuery = true)
    List<Valuation> findByEvaluationId(Integer id);
}
