package com.jtk.ps.api.model;

import java.time.LocalDate;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "course_values")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseValues {
    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "value")
    private Float value;

    // @Column(name = "criteria_id")
    // private Integer criteriaId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "criteria_id")
    private CriteriaComponentCourse criteriaComponentCourse;

    // @Column(name = "")
    // private Integer mentorValuesId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mentor_values_id")
    private SupervisorGradeResult mentorValues;

    // @Column(name = "")
    // private Integer selfAssessmentValuesId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "self_assessment_values_id")
    private SelfAssessmentGrade selfAssessmentValues;

    // @Column(name = "seminar_values_id")
    // private Integer seminarValuesId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seminar_values_id")
    private SeminarValues seminarValues;

    // @Column(name = "industry_values_id")
    // private Integer industryValuesId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "industry_values_id")
    private Valuation valuation;

    // @Column(name = "participant_id")
    // private Integer participantId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participant_id")
    private Participant participant;

    @Column(name = "created_date")
    private LocalDate created_date;
}
