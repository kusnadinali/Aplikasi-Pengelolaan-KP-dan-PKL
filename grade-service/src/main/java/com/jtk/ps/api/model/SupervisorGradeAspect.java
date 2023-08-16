package com.jtk.ps.api.model;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "supervisor_grade_aspect")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SupervisorGradeAspect {
    
    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "grade_weight")
    private Float gradeWeight;

    @Column(name = "prodi_id")
    private Integer prodiId;

    @Column(name = "status")
    private Integer status;
}
