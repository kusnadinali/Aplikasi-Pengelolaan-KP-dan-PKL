package com.jtk.ps.api.model;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "self_assessment_grade")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SelfAssessmentGrade {
    
    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "grade_self_assessment")
    private Integer valueSelfAssessment;

    // @Column(name = "self_assessment_id")
    // private Integer selfAssessmentId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "self_assessment_id")
    private SelfAssessment selfAssessment;

    // @Column(name = "criteria_self_assessment_id")
    // private Integer criteriaSelfAssessmentId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "criteria_self_assessment_id")
    private SelfAssessmentAspect selfAssessmentAspect;

}
