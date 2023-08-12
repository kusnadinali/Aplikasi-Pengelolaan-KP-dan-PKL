package com.jtk.ps.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EtproValueDto {
    private Integer id;

    private Float value;

    private EtproAspectDto aspect;

    private EtproFormDto form;
}
