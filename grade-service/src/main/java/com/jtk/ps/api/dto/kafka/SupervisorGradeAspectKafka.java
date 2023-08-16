package com.jtk.ps.api.dto.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupervisorGradeAspectKafka {
    private Integer id;

    private String name;

    private Float gradeWeight;

    private Integer prodiId;

    private Integer status;

    private String operation;
}
