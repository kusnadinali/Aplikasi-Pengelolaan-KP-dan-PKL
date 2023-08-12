package com.jtk.ps.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EtproAspectDto {
    
    private Integer id;

    @JsonProperty("aspek_name")
    private String aspekName;

    @JsonProperty("aspek_bobot")
    private Float aspekBobot;

    @JsonProperty("is_deleted")
    private Integer isDeleted;

    @JsonProperty("prodi_id")
    private Integer prodiId;
}
