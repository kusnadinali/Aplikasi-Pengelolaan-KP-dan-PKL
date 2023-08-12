package com.jtk.ps.api.model;

import java.time.LocalDateTime;

import javax.persistence.*;

import org.springframework.data.annotation.CreatedDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "etpro_teori_form")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EtproTeoriForm {
    
    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "prodi_id")
    private Integer prodiId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participant_id")
    private Participant participant;

    @Column(name = "examiner_name")
    private String examinerName;

    @CreatedDate
    @Column(name = "date", updatable = false)
    private LocalDateTime date;

    @Column(name = "is_finalization")
    private Integer isFinalization;

    @Column(name = "total_value")
    private Float totalValue;

    private Integer year;
}
