package com.jtk.ps.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jtk.ps.api.model.EtproTeoriAspect;

@Repository
public interface EtproTeoriAspectRepository extends JpaRepository<EtproTeoriAspect, Integer>{
    
    List<EtproTeoriAspect> findByProdiIdAndIsDeleted(Integer prodiId, Integer isDeleted);

    
}
