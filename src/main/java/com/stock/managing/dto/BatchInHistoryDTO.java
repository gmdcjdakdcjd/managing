package com.stock.managing.dto;

import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BatchInHistoryDTO {

    private Long histId;
    private Long jobId;
    private String jobName;
    private String jobInfo;

    private LocalDateTime execStartTime;
    private LocalDateTime execEndTime;
    private String execStatus;
    private String execMessage;

    private LocalDate execDate;
    private Long durationMs;

    private String type;
}
