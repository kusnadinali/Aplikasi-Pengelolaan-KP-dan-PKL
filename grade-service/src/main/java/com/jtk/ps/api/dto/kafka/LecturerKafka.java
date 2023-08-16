package com.jtk.ps.api.dto.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LecturerKafka {

    private Integer id;

    private String name;

    private Integer prodiId;

    private Integer account_id;

    private String operation;
}
