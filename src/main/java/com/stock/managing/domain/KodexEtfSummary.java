package com.stock.managing.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "kodex_etf_summary", schema = "managing")
public class KodexEtfSummary {

    @EmbeddedId
    private KodexEtfSummaryId id;

    @Column(name = "etf_name", length = 100, nullable = false)
    private String etfName;

    @Column(name = "irp_yn", length = 20)
    private String irpYn;

    @Column(name = "total_cnt", nullable = false)
    private Integer totalCnt;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
