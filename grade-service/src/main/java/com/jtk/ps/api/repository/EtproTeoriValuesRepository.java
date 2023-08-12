package com.jtk.ps.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jtk.ps.api.model.EtproTeoriForm;
import com.jtk.ps.api.model.EtproTeoriValues;
import com.jtk.ps.api.model.Participant;

@Repository
public interface EtproTeoriValuesRepository extends JpaRepository<EtproTeoriValues, Integer>{
    
    List<EtproTeoriValues> findByForm(EtproTeoriForm form);

    List<EtproTeoriValues> findByForm_Participant(Participant participant);
}