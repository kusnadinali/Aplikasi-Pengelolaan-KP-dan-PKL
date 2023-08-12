package com.jtk.ps.api.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeminarFormResponse {
    private Integer id;

    private Date dateSeminar;

    private ParticipantDto participant;

    private AccountDto examiner;
    
    private Integer examinerType;

    private String comment;

    private Integer isFinalization;

    private Float totalValue;
}
