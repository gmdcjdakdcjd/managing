package com.stock.managing.dto;

import com.stock.managing.domain.BatchOut;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BatchOutDTO {

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

    private String shellFileDir;

    private Boolean isActive;
    private LocalDateTime createdAt;

    public static BatchOutDTO fromEntity(BatchOut out) {
        return BatchOutDTO.builder()
                .jobId(out.getJobId())
                .jobName(out.getJobName())
                .jobInfo(out.getJobInfo())
                .scheduleGb(out.getScheduleGb())
                .jobMonth(out.getJobMonth())
                .jobDay(out.getJobDay())
                .jobWeek(out.getJobWeek())
                .jobHour(out.getJobHour())
                .jobMin(out.getJobMin())
                .actGb(out.getActGb())
                .lastExecInfo(out.getLastExecInfo())
                .nextExecInfo(out.getNextExecInfo())
                .shellFileDir(out.getShellFileDir())
                .isActive(out.getIsActive())
                .createdAt(out.getCreatedAt())
                .build();
    }

}
