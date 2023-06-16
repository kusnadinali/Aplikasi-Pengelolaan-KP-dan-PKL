package com.jtk.ps.api.dto.kafka;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SelfAssessmentGradeKafka {
    private Integer id;

    private Integer gradeSelfAssessment;

    private Integer selfAssessmentId;

    private Integer criteriaSelfAssessmentId;


    private Integer participantId;

    private String operation;
}
