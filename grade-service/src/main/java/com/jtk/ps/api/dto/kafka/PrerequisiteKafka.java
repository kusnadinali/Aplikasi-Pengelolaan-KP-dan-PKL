package com.jtk.ps.api.dto.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrerequisiteKafka {
    private Integer id;

    private Integer year;

    private Integer company_id;

    private String operation;
}
