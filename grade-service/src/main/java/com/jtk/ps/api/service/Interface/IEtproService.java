package com.jtk.ps.api.service.Interface;

import java.io.ByteArrayInputStream;
import java.util.List;

import com.jtk.ps.api.dto.EtproAspectDto;
import com.jtk.ps.api.dto.EtproParticipantDto;
import com.jtk.ps.api.dto.EtproRecapitulationResponse;
import com.jtk.ps.api.dto.EtproUpdateValuesDto;

public interface IEtproService {
    
    List<EtproAspectDto> getAllAspect(Integer prodiId);

    void updateAspect(List<EtproAspectDto> request);

    void createAspect(EtproAspectDto request);

    List<EtproParticipantDto> getAllForm(Integer prodiId); //ambil form per peserta

    // void getForm(Integer idForm);

    void updateFormAndValues(EtproUpdateValuesDto request);

    List<EtproRecapitulationResponse> getRecapitulation(Integer prodiId);
    ByteArrayInputStream loadEtpro(Integer prodiId);
}
