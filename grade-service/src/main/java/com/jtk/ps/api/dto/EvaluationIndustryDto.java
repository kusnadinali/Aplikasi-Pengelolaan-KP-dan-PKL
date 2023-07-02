package com.jtk.ps.api.dto;


import java.util.List;

import com.jtk.ps.api.model.Company;
import com.jtk.ps.api.model.Participant;
import com.jtk.ps.api.model.Valuation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EvaluationIndustryDto {    
    private Integer id;

    private String comment;

    private Integer year;

    private Integer numEvaluation;

    private String position;

    private Integer prodiId;

    private Company company;

    private Participant participant;

    private List<Valuation> valuations;
}
