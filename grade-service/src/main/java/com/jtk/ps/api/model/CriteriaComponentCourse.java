package com.jtk.ps.api.model;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "criteria_component_course")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CriteriaComponentCourse {
    
    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name_form", nullable = false)
    private String nameForm;

    @Column(name = "type_form", nullable = false)
    private String typeForm;

    @Column(name = "bobot_criteria", nullable = false)
    private Integer bobotCriteria;

    // @Column(name = "component_id", nullable = false)
    // private Integer componentId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "component_id")
    private ComponentCourse componentCourse;

    // @Column(name = "industry_criteria_id")
    // private Integer industryCriteriaId;
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "industry_criteria_id", nullable = true)
    private AssessmentAspect industryCriteria;

    // @Column(name = "seminar_criteria_id")
    // private Integer seminarCriteriaId;
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "seminar_criteria_id", nullable = true)
    private SeminarCriteria seminarCriteria;

    // @Column(name = "self_assessment_criteria_id")
    // private Integer selfAssessmentCriteriaId;
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "self_assessment_criteria_id", nullable = true)
    private SelfAssessmentAspect selfAssessmentCriteria;

    // @Column(name = "supervisor_criteria_id")
    // private Integer supervisorCriteriaId;
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "supervisor_criteria_id", nullable = true)
    private SupervisorGradeAspect supervisorCriteria;

    @Column(name = "is_deleted", nullable = false)
    private Integer isDeleted;

}
