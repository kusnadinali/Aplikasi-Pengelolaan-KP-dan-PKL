package com.jtk.ps.api.model;

import java.sql.Date;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "deadline")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Deadline {
    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "dayRange")
    private Integer dayRange;

    @Column(name = "start_assignment_date")
    private Date startDate;

    @Column(name = "finish_assignment_date")
    private Date finishDate;
}
