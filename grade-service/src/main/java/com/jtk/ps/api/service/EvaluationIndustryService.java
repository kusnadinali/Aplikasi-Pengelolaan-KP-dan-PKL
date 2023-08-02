package com.jtk.ps.api.service;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.jtk.ps.api.dto.EvaluationDto;
import com.jtk.ps.api.dto.ParticipantEvaluationDto;
import com.jtk.ps.api.dto.ValuationV2Dto;
import com.jtk.ps.api.model.Evaluation;
import com.jtk.ps.api.model.Participant;
import com.jtk.ps.api.model.ValuationV2;
import com.jtk.ps.api.repository.AccountRepository;
import com.jtk.ps.api.repository.EvaluationRepository;
import com.jtk.ps.api.repository.ParticipantRepository;
import com.jtk.ps.api.repository.ValuationV2Repository;
import com.jtk.ps.api.service.Interface.IEvaluationIndustryService;

@Service
public class EvaluationIndustryService implements IEvaluationIndustryService{
    @Autowired
    @Lazy
    private ValuationV2Repository valuationV2Repository;

    @Autowired
    @Lazy
    private EvaluationRepository evaluationRepository;

    @Autowired
    @Lazy
    private ParticipantRepository participantRepository;

    @Autowired
    @Lazy
    private AccountRepository accountRepository;
    
    @Override
    public List<ValuationV2Dto> getAllValuationV2ByEvaluation(Integer evaluationId) {
        List<ValuationV2> listValuation = valuationV2Repository.findByEvaluationId(evaluationId);
        List<ValuationV2Dto> response = new ArrayList<>();

        listValuation.forEach(v -> {
            ValuationV2Dto temp = new ValuationV2Dto();
            temp.setAspectName(v.getAspectName());
            temp.setEvaluationId(v.getEvaluation().getId());
            temp.setId(v.getId());
            temp.setJustification(v.getJustification());
            temp.setLetterValue(v.getLetterValue());
            temp.setNumericValue(v.getNumericValue());

            response.add(temp);
        });
        return response;
    }

    @Override
    public List<ParticipantEvaluationDto> getAllEvaluation(Integer prodiId) {
        
        // List<EvaluationDto> response = new ArrayList<>();
        List<ParticipantEvaluationDto> responses = new ArrayList<>();
        List<Participant> participants = participantRepository.findAllByYearAndProdi(Integer.parseInt(Year.now().toString()), prodiId);
        
        participants.forEach(p -> {
            ParticipantEvaluationDto temp1 = new ParticipantEvaluationDto();
            List<EvaluationDto> tempEvaluation = new ArrayList<>();
            temp1.setId(p.getId());

            temp1.setNim(p.getAccount().getUsername());

            temp1.setName(p.getName());

            List<Evaluation> listEvaluations = evaluationRepository.findByParticipantId(p.getId());
            for (Evaluation e : listEvaluations) {
                EvaluationDto temp = new EvaluationDto();
                temp.setComment(e.getComment());
                temp.setCompanyId(e.getCompany().getId());
                temp.setId(e.getId());
                temp.setNumEvaluation(e.getNumEvaluation());
                temp.setParticipantId(e.getParticipant().getId());
                temp.setPosition(e.getPosition());
                temp.setProdiId(e.getProdiId());
                temp.setStatus(e.getStatus());
                temp.setYear(e.getYear());
    
                tempEvaluation.add(temp);
            }
            temp1.setEvaluations(tempEvaluation);

            responses.add(temp1);
        });

        
        return responses;
    }

    @Override
    public void quantizationEvaluation(List<ValuationV2Dto> valuations, Integer idEvaluation) {
        
        for (ValuationV2Dto v : valuations) {
            Optional<ValuationV2> valuationV2 = valuationV2Repository.findById(v.getId());

            valuationV2.ifPresent(c -> {
                c.setNumericValue(v.getNumericValue());

                valuationV2Repository.save(c);
            });
        }
        
    }

    
}
