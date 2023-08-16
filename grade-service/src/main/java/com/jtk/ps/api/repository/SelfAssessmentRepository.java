package com.jtk.ps.api.repository;

import java.sql.Date;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jtk.ps.api.model.Participant;
import com.jtk.ps.api.model.SelfAssessment;

@Repository
public interface SelfAssessmentRepository extends JpaRepository<SelfAssessment, Integer>{
    Optional<SelfAssessment> findByStartDateAndParticipant(Date startDate, Participant participant);
}
