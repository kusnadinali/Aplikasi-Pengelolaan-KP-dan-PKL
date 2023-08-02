package com.jtk.ps.api.model;

import java.util.Date;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "supervisor_grade")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SupervisorGrade {
    
    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer phase;

    // @Column(name = "participant_id")
    // private Integer participantId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participant_id")
    private Participant participant;

    private Date date;

    // @Column(name = "supervisor_id_grade")
    // private Integer supervisorGradeId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supervisor_id_grade")
    private SupervisorMapping supervisor;
}
