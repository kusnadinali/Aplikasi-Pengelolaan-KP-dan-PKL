package com.jtk.ps.api.model;

import java.util.Date;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "supervisor_mapping")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SupervisorMapping {
    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // @Column(name = "company_id_mapping")
    // private Integer companyMappingId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    // @Column(name = "participant_id_mapping")
    // private Integer participantMappingId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participant_id")
    private Participant participant;

    // @Column(name = "supervisor_id_mapping")
    // private Integer supervisorMappingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecturer_id")
    private Lecturer lecturer;

    @Column(name = "prodi_id")
    private Integer prodiId;

    @Column(name = "mapping_date")
    private Date mappingDate;
}
