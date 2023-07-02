package com.jtk.ps.api.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParticipantEvaluationDto {
    private Integer id;

    private String nim;
    
    private String name;

    private List<EvaluationDto> evaluations;
}
