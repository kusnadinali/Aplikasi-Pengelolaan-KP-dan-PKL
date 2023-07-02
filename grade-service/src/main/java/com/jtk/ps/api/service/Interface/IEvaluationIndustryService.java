package com.jtk.ps.api.service.Interface;

import java.util.List;

import com.jtk.ps.api.dto.ParticipantEvaluationDto;
import com.jtk.ps.api.dto.ValuationV2Dto;

public interface IEvaluationIndustryService {
    
    List<ValuationV2Dto> getAllValuationV2ByEvaluation(Integer evaluationId);

    List<ParticipantEvaluationDto> getAllEvaluation(Integer prodiId);

    void quantizationEvaluation(List<ValuationV2Dto> valuations, Integer idEvaluation);
}
