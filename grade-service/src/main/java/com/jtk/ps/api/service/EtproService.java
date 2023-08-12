package com.jtk.ps.api.service;

import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.jtk.ps.api.dto.EtproAspectDto;
import com.jtk.ps.api.dto.EtproFormDto;
import com.jtk.ps.api.dto.EtproParticipantDto;
import com.jtk.ps.api.dto.EtproRecapitulationResponse;
import com.jtk.ps.api.dto.EtproUpdateValuesDto;
import com.jtk.ps.api.dto.EtproValueDto;
import com.jtk.ps.api.dto.ParticipantDto;
import com.jtk.ps.api.helper.ExcelHelper;
import com.jtk.ps.api.model.EtproTeoriAspect;
import com.jtk.ps.api.model.EtproTeoriForm;
import com.jtk.ps.api.model.EtproTeoriValues;
import com.jtk.ps.api.model.Participant;
import com.jtk.ps.api.repository.EtproTeoriAspectRepository;
import com.jtk.ps.api.repository.EtproTeoriFormRepository;
import com.jtk.ps.api.repository.EtproTeoriValuesRepository;
import com.jtk.ps.api.repository.ParticipantRepository;
import com.jtk.ps.api.service.Interface.IEtproService;

@Service
public class EtproService implements IEtproService {

    @Lazy
    @Autowired
    private EtproTeoriAspectRepository etproTeoriAspectRepository;

    @Lazy
    @Autowired
    private EtproTeoriFormRepository etproTeoriFormRepository;

    @Lazy
    @Autowired
    private EtproTeoriValuesRepository etproTeoriValuesRepository;

    @Lazy
    @Autowired
    private ParticipantRepository participantRepository;

    @Override
    public void createAspect(EtproAspectDto request) {
        EtproTeoriAspect newAspect = new EtproTeoriAspect();

        newAspect.setAspekBobot(0f);
        newAspect.setAspekName(request.getAspekName());
        newAspect.setIsDeleted(0);
        newAspect.setProdiId(request.getProdiId());

        newAspect = etproTeoriAspectRepository.save(newAspect);

        Integer prodiId = newAspect.getProdiId();

        List<EtproTeoriForm> forms = etproTeoriFormRepository.findByProdiIdAndYear(prodiId, Integer.valueOf(Year.now().toString()));

        if (forms.size() == 0) {
            List<Participant> participants = participantRepository
                    .findAllByYearAndProdi(Integer.valueOf(Year.now().toString()), prodiId);

            participants.forEach(p -> {
                EtproTeoriForm newForm = new EtproTeoriForm();

                newForm.setIsFinalization(0);
                newForm.setParticipant(p);
                newForm.setProdiId(prodiId);
                newForm.setTotalValue(0f);
                newForm.setYear(Integer.valueOf(Year.now().toString()));

                newForm = etproTeoriFormRepository.save(newForm);

                final EtproTeoriForm tempForm = newForm;

                List<EtproTeoriAspect> aspects = etproTeoriAspectRepository.findByProdiIdAndIsDeleted(prodiId, 0);
                aspects.forEach(a -> {
                    EtproTeoriValues value = new EtproTeoriValues();

                    value.setAspect(a);
                    value.setForm(tempForm);
                    value.setValue(0f);

                    value = etproTeoriValuesRepository.save(value);
                });
            });
        } else {
            for( EtproTeoriForm f :forms){
                EtproTeoriValues value = new EtproTeoriValues();
                value.setAspect(newAspect);
                value.setForm(f);
                value.setValue(0f);

                etproTeoriValuesRepository.save(value);
            }
        }
    }

    @Override
    public List<EtproAspectDto> getAllAspect(Integer prodiId) {
        List<EtproAspectDto> response = new ArrayList<>();
        List<EtproTeoriAspect> aspects = etproTeoriAspectRepository.findByProdiIdAndIsDeleted(prodiId, 0);
        aspects.forEach(a -> {
            EtproAspectDto item = new EtproAspectDto();
            item.setAspekBobot(a.getAspekBobot());
            item.setAspekName(a.getAspekName());
            item.setId(a.getId());
            item.setIsDeleted(a.getIsDeleted());
            item.setProdiId(a.getProdiId());

            response.add(item);
        });
        return response;
    }

    @Override
    public void updateAspect(List<EtproAspectDto> request) {
        request.forEach(item -> {
            EtproTeoriAspect aspect = new EtproTeoriAspect();

            if (item.getId() != null) {
                aspect.setAspekBobot(item.getAspekBobot());
                aspect.setAspekName(item.getAspekName());
                aspect.setId(item.getId());
                aspect.setIsDeleted(item.getIsDeleted());
                aspect.setProdiId(item.getProdiId());

                etproTeoriAspectRepository.save(aspect);
            }
        });
    }

    @Override
    public List<EtproParticipantDto> getAllForm(Integer prodiId) {
        List<EtproTeoriForm> forms = etproTeoriFormRepository.findByProdiIdAndYear(prodiId,
                Integer.valueOf(Year.now().toString()));

        List<EtproParticipantDto> response = new ArrayList<>();

        if (forms.size() == 0) {
            List<Participant> participants = participantRepository
                    .findAllByYearAndProdi(Integer.valueOf(Year.now().toString()), prodiId);

            participants.forEach(p -> {
                EtproTeoriForm newForm = new EtproTeoriForm();
                EtproParticipantDto itemResponse = new EtproParticipantDto();

                newForm.setIsFinalization(0);
                newForm.setParticipant(p);
                newForm.setProdiId(prodiId);
                newForm.setTotalValue(0f);
                newForm.setYear(Integer.valueOf(Year.now().toString()));

                newForm = etproTeoriFormRepository.save(newForm);

                EtproFormDto formDto = new EtproFormDto();
                formDto.setDate(newForm.getDate());
                formDto.setExaminerName(newForm.getExaminerName());
                formDto.setId(newForm.getId());
                formDto.setIsFinalization(newForm.getIsFinalization());

                ParticipantDto participantDto = new ParticipantDto();
                participantDto.setAccount_id(newForm.getParticipant().getAccount().getId());
                participantDto.setId(newForm.getParticipant().getId());
                participantDto.setName(newForm.getParticipant().getName());
                participantDto.setNim(newForm.getParticipant().getAccount().getUsername());
                participantDto.setProdi_id(newForm.getParticipant().getProdiId());
                participantDto.setStatus_cv(newForm.getParticipant().getStatusCv());
                participantDto.setYear(newForm.getParticipant().getYear());

                formDto.setParticipant(participantDto);
                formDto.setProdiId(newForm.getProdiId());
                formDto.setTotalValue(newForm.getTotalValue());
                formDto.setYear(newForm.getYear());
                itemResponse.setForm(formDto);

                final EtproTeoriForm tempForm = newForm;
                List<EtproValueDto> valueDtos = new ArrayList<>();

                List<EtproTeoriAspect> aspects = etproTeoriAspectRepository.findByProdiIdAndIsDeleted(prodiId, 0);
                aspects.forEach(a -> {
                    EtproTeoriValues value = new EtproTeoriValues();

                    value.setAspect(a);
                    value.setForm(tempForm);
                    value.setValue(0f);

                    value = etproTeoriValuesRepository.save(value);

                    EtproAspectDto aspectDto = new EtproAspectDto();
                    aspectDto.setAspekBobot(a.getAspekBobot());
                    aspectDto.setAspekName(a.getAspekName());
                    aspectDto.setId(a.getId());
                    aspectDto.setIsDeleted(a.getIsDeleted());
                    aspectDto.setProdiId(a.getProdiId());

                    EtproValueDto valueDto = new EtproValueDto();
                    valueDto.setAspect(aspectDto);
                    valueDto.setForm(formDto);
                    valueDto.setId(value.getId());
                    valueDto.setValue(value.getValue());

                    valueDtos.add(valueDto);
                });
                itemResponse.setValues(valueDtos);
                response.add(itemResponse);
            });
            return response;
        } else {
            forms.forEach(f -> {
                EtproParticipantDto itemResponse = new EtproParticipantDto();
                EtproFormDto formDto = new EtproFormDto();
                formDto.setDate(f.getDate());
                formDto.setExaminerName(f.getExaminerName());
                formDto.setId(f.getId());
                formDto.setIsFinalization(f.getIsFinalization());

                ParticipantDto participantDto = new ParticipantDto();
                participantDto.setAccount_id(f.getParticipant().getAccount().getId());
                participantDto.setId(f.getParticipant().getId());
                participantDto.setName(f.getParticipant().getName());
                participantDto.setNim(f.getParticipant().getAccount().getUsername());
                participantDto.setProdi_id(f.getParticipant().getProdiId());
                participantDto.setStatus_cv(f.getParticipant().getStatusCv());
                participantDto.setYear(f.getParticipant().getYear());

                formDto.setParticipant(participantDto);
                formDto.setProdiId(f.getProdiId());
                formDto.setTotalValue(f.getTotalValue());
                formDto.setYear(f.getYear());
                itemResponse.setForm(formDto);

                List<EtproTeoriValues> values = etproTeoriValuesRepository.findByForm(f);
                List<EtproValueDto> valueDtos = new ArrayList<>();
                values.forEach(v -> {
                    EtproValueDto valueDto = new EtproValueDto();
                    EtproAspectDto aspectDto = new EtproAspectDto();

                    aspectDto.setAspekBobot(v.getAspect().getAspekBobot());
                    aspectDto.setAspekName(v.getAspect().getAspekName());
                    aspectDto.setId(v.getAspect().getId());
                    aspectDto.setIsDeleted(v.getAspect().getIsDeleted());
                    aspectDto.setProdiId(v.getAspect().getProdiId());

                    valueDto.setAspect(aspectDto);
                    valueDto.setForm(formDto);
                    valueDto.setId(v.getId());
                    valueDto.setValue(v.getValue());

                    valueDtos.add(valueDto);
                });
                itemResponse.setValues(valueDtos);
                response.add(itemResponse);
            });
            return response;
        }
    }

    @Override
    public void updateFormAndValues(EtproUpdateValuesDto request) {

        etproTeoriFormRepository.findById(request.getIdForm()).ifPresent(f -> {
            Float total = 0f;
            f.setDate(LocalDateTime.now());
            f.setExaminerName(request.getExaminerName());

            for (EtproValueDto valueDto : request.getValueDtos()) {
                etproTeoriValuesRepository.findById(valueDto.getId()).ifPresent(v -> {
                    v.setValue(valueDto.getValue());

                    etproTeoriValuesRepository.save(v);
                });
                total += (valueDto.getValue() * valueDto.getAspect().getAspekBobot()) / 100;
            }
            f.setTotalValue(total);

            etproTeoriFormRepository.save(f);
        });

    }

    @Override
    public List<EtproRecapitulationResponse> getRecapitulation(Integer prodiId) {

        List<Participant> participants = participantRepository
                .findByYearAndProdiId(Integer.valueOf(Year.now().toString()), prodiId);
        List<EtproRecapitulationResponse> responses = new ArrayList<>();

        participants.forEach(p -> {
            List<EtproTeoriValues> values = etproTeoriValuesRepository.findByForm_Participant(p);
            EtproRecapitulationResponse response = new EtproRecapitulationResponse();
            response.setParticipantDto(new ParticipantDto(p.getId(), p.getName(), p.getYear(), p.getStatusCv(),
                    p.getProdiId(), p.getAccount().getId(), p.getAccount().getUsername()));
            
            List<EtproValueDto> valueDtos = new ArrayList<>();

            values.forEach(v -> {

                response.setTotal(v.getForm().getTotalValue());
                EtproValueDto valueDto = new EtproValueDto();
                EtproAspectDto aspectDto = new EtproAspectDto();
                EtproFormDto formDto = new EtproFormDto();

                aspectDto.setAspekBobot(v.getAspect().getAspekBobot());
                aspectDto.setAspekName(v.getAspect().getAspekName());
                aspectDto.setId(v.getAspect().getId());
                aspectDto.setIsDeleted(v.getAspect().getIsDeleted());
                aspectDto.setProdiId(v.getAspect().getProdiId());

                formDto.setDate(v.getForm().getDate());
                formDto.setExaminerName(v.getForm().getExaminerName());
                formDto.setId(v.getForm().getId());
                formDto.setIsFinalization(v.getForm().getIsFinalization());
                formDto.setParticipant(response.getParticipantDto());
                formDto.setProdiId(v.getForm().getProdiId());
                formDto.setTotalValue(v.getForm().getTotalValue());
                formDto.setYear(v.getForm().getYear());

                valueDto.setAspect(aspectDto);
                valueDto.setForm(formDto);
                valueDto.setId(v.getId());
                valueDto.setValue(v.getValue());
                
                valueDtos.add(valueDto);
            });
            response.setValues(valueDtos);
            responses.add(response);
        });

        return responses;
    }

    @Override
    public ByteArrayInputStream loadEtpro(Integer prodiId) {
        List<EtproRecapitulationResponse> recap = getRecapitulation(prodiId);
        // List<SeminarCriteria> criterias = seminarCriteriaRepository.findAllBySelected();
        // List<List<SeminarValueParticipantDto>> Llist = new ArrayList<>();
        // for(int i = 0; i < 3; i++){
        //     List<SeminarValueParticipantDto> list = this.getRecapitulationByTypeForm(year, prodiId, i+1);
        //     Llist.add(list);
        // }
        // List<SeminarTotalValueDto> listTotal = this.getRecapitulationTotal(year, prodiId);
        ByteArrayInputStream in = ExcelHelper.recapEtprotoExcel(recap);
        return in;
    }
}
