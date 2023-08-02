package com.jtk.ps.api.model;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "total_courses")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TotalCourses {
    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // @Column(name = "course_id")
    // private Integer courseId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private CourseForm courseForm;

    // @Column(name = "participant_id")
    // private Integer participantId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participant_id")
    private Participant participant;

    private Float value;
}
