package com.jtk.ps.api.dto;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EtproUpdateValuesDto {
    
    private Integer idForm;
    private String examinerName;
    private List<EtproValueDto> valueDtos;
}
