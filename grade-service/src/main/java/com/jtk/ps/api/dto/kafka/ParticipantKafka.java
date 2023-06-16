package com.jtk.ps.api.dto.kafka;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ParticipantKafka {
    
    private Integer id;

    private String name;

    private Integer year;

    private boolean status_cv;

    private Integer prodi_id;

    private Integer account_id;

    private String operation;
}
