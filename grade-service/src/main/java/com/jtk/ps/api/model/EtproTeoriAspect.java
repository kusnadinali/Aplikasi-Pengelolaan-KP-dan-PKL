package com.jtk.ps.api.model;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "etpro_teori_aspect")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EtproTeoriAspect {
    
    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "aspek_name")
    private String aspekName;

    @Column(name = "aspek_bobot")
    private Float aspekBobot;

    @Column(name = "is_deleted")
    private Integer isDeleted;

    @Column(name = "prodi_id")
    private Integer prodiId;
}
