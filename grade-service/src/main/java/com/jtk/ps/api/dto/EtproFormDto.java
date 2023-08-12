package com.jtk.ps.api.dto;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EtproFormDto {
    private Integer id;

    private Integer prodiId;

    private ParticipantDto participant;

    private String examinerName;

    private LocalDateTime date;

    private Integer isFinalization;

    private Float totalValue;

    private Integer year;
}
