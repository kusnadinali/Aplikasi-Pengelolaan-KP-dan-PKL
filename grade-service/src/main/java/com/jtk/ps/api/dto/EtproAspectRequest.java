package com.jtk.ps.api.dto;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EtproAspectRequest {
    private List<EtproAspectDto> aspect;
}
