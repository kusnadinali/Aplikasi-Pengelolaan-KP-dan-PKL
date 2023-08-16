package com.jtk.ps.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jtk.ps.api.model.Deadline;

@Repository
public interface DeadlineRepository extends JpaRepository<Deadline, Integer>{
    Optional<Deadline> findByName(String name);
}
