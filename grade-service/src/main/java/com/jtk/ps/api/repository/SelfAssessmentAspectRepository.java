package com.jtk.ps.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jtk.ps.api.model.SelfAssessmentAspect;

@Repository
public interface SelfAssessmentAspectRepository extends JpaRepository<SelfAssessmentAspect,Integer>{
    
    List<SelfAssessmentAspect> findByStatus(Integer status);
}
