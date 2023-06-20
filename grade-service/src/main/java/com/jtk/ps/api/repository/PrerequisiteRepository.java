package com.jtk.ps.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jtk.ps.api.model.Prerequisite;

@Repository
public interface PrerequisiteRepository extends JpaRepository<Prerequisite, Integer>{
    List<Prerequisite> findByYear(Integer year);
}
