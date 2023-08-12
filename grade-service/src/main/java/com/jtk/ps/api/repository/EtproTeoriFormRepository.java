package com.jtk.ps.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jtk.ps.api.model.EtproTeoriForm;

@Repository
public interface EtproTeoriFormRepository extends JpaRepository<EtproTeoriForm, Integer>{
    
    List<EtproTeoriForm> findByProdiIdAndYear(Integer prodiId, Integer year);
}
