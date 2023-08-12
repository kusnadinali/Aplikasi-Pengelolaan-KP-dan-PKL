package com.jtk.ps.api.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeminarValuesResponseDto {
    private Integer id;

    private Float value;

    private SeminarCriteriaDto seminarCriteria;

    private SeminarFormResponse seminarForm;
}
