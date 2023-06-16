package com.jtk.ps.api.dto.kafka;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SupervisorGradeAspectKafka {
    private Integer id;

    private String description;

    private Float gradeWeight;

    private String operation;
}
