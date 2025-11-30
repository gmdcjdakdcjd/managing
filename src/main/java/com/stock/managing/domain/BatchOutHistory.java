package com.stock.managing.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "batch_out_h")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BatchOutHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hist_id")
    private Long histId;

    @Column(name = "job_id", nullable = false)
    private Long jobId; // FK (ID만 저장)

    @Column(name = "job_name", nullable = false, length = 100)
    private String jobName;

    @Column(name = "job_info", length = 255)
    private String jobInfo;

    @Column(name = "exec_start_time", nullable = false)
    private LocalDateTime execStartTime;

    @Column(name = "exec_end_time")
    private LocalDateTime execEndTime;

    @Column(name = "exec_status", nullable = false, length = 20)
    private String execStatus;

    @Column(name = "exec_message", columnDefinition = "TEXT")
    private String execMessage;

    @Column(name = "exec_date", nullable = false)
    private LocalDate execDate;

    @Column(name = "duration_ms")
    private Long durationMs;
}
