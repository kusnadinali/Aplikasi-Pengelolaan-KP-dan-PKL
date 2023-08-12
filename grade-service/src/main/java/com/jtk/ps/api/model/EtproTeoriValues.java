package com.jtk.ps.api.model;

import javax.persistence.*;

import lombok.*;

@Entity
@Table(name = "etpro_teori_values")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EtproTeoriValues {
    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "value")
    private Float value;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aspect_id")
    private EtproTeoriAspect aspect;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "form_id")
    private EtproTeoriForm form;
}
