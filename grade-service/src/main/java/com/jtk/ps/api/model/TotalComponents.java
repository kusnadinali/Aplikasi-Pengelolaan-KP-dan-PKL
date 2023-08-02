package com.jtk.ps.api.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "total_components")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TotalComponents {
    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // @Column(name = "component_id")
    // private Integer componentId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "component_id")
    private ComponentCourse componentCourse;

    // @Column(name = "participant_id")
    // private Integer participantId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participant_id")
    private Participant participant;

    private Float value;
}
