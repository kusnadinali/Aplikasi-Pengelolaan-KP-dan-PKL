package com.jtk.ps.api.dto;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EtproRecapitulationResponse {
    
    private ParticipantDto participantDto;

    private List<EtproValueDto> values;

    private Float total;
}
