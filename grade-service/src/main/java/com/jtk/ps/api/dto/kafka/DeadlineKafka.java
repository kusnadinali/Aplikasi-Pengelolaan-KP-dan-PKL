package com.jtk.ps.api.dto.kafka;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeadlineKafka {
    private Integer id;

    private String name;

    private Integer dayRange;

    private Date startDate;

    private Date finishDate;

    private String operation;
}
