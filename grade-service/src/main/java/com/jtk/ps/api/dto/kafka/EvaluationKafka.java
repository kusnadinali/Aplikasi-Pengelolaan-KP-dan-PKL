package com.jtk.ps.api.dto.kafka;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EvaluationKafka {
    
    private Integer id;

    private String comment;

    private Integer year;

    private Integer num_evaluation;

    private Integer status;

    private String position;

    private Integer prodi_id;

    private Integer company_id;

    private Integer participant_id;

    private String operation;
}
