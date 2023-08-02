package com.jtk.ps.api.model;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "supervisor_grade_result")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SupervisorGradeResult {
    
    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "grade")
    private Integer value;

    // @Column(name = "supervisor_grade_id")
    // private Integer supervisorGradeId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supervisor_grade_id")
    private SupervisorGrade supervisorGrade;

    // @Column(name = "id_aspect_grade")
    // private Integer aspectId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_aspect_grade")
    private SupervisorGradeAspect supervisorGradeAspect;
}
