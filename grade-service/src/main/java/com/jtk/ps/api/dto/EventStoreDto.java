package com.jtk.ps.api.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventStoreDto {
    
    private Long id;

    private String entityId;

    private String eventType;

    private LocalDateTime eventTime;
}
