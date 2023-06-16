package com.jtk.ps.api.dto.kafka;

import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SupervisorMappingKafka {
    
    private Integer id;

    private Integer companyMappingId;

    private Integer participantMappingId;

    private Integer supervisorMappingId;

    private Integer prodiId;

    private Date mappingDate;

    private String operation;
}
