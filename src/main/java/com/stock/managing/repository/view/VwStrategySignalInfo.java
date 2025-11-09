package com.stock.managing.repository.view;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Immutable;

@Entity
@Table(name = "vw_strategy_signal_info")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Immutable // ✅ View라 수정 불가
public class VwStrategySignalInfo {

    @Id
    @Column(name = "result_id")
    private Long resultId; // view에서 고유하게 식별 가능한 컬럼

    private String name;           // 종목명
    private String code;           // 종목코드
    @Column(name = "strategy_name")
    private String strategyName;   // 전략명
    @Column(name = "signal_date")
    private String signalDate;     // 포착일
}