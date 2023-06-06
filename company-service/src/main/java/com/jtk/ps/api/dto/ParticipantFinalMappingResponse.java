package com.jtk.ps.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParticipantFinalMappingResponse {
    @JsonProperty("participant_id")
    private Integer participantId;
    
    @JsonProperty("prodi_id")
    private Integer idProdi;
}
