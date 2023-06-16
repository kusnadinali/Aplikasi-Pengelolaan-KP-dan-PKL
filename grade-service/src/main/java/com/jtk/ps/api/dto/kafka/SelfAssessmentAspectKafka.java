package com.jtk.ps.api.dto.kafka;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SelfAssessmentAspectKafka {
    private Integer id;

    private String name;

    private String description;

    private String operation;
}
