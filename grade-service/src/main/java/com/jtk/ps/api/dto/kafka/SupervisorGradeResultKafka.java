package com.jtk.ps.api.dto.kafka;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SupervisorGradeResultKafka {
    
    private Integer id;

    private Integer value;

    private Integer supervisorGradeId;

    private Integer aspectId;

    private String operation;
}
