package com.stock.managing.dto;

import com.stock.managing.domain.BatchIn;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BatchInDTO {

    private Long jobId;
    private String jobName;
    private String jobInfo;
    private String scheduleGb;

    private String jobMonth;
    private String jobDay;
    private String jobWeek;
    private String jobHour;
    private String jobMin;

    private String actGb;
    private String lastExecInfo;
    private String nextExecInfo;

    private String filePattern;

    private Boolean isActive;
    private LocalDateTime createdAt;

    public static BatchInDTO fromEntity(BatchIn in) {
        return BatchInDTO.builder()
                .jobId(in.getJobId())
                .jobName(in.getJobName())
                .jobInfo(in.getJobInfo())
                .scheduleGb(in.getScheduleGb())
                .jobMonth(in.getJobMonth())
                .jobDay(in.getJobDay())
                .jobWeek(in.getJobWeek())
                .jobHour(in.getJobHour())
                .jobMin(in.getJobMin())
                .actGb(in.getActGb())
                .lastExecInfo(in.getLastExecInfo())
                .nextExecInfo(in.getNextExecInfo())
                .filePattern(in.getFilePattern())
                .isActive(in.getIsActive())
                .createdAt(in.getCreatedAt())
                .build();
    }

}
