package com.stock.managing.repository;

import com.stock.managing.domain.StrategyResult;
import com.stock.managing.domain.StrategyResultId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StrategyResultRepository extends JpaRepository<StrategyResult, StrategyResultId> {

    // 특정 전략명 + 특정 날짜 존재 여부
    boolean existsByStrategyNameAndSignalDate(String strategyName, String signalDate);

    // 오늘 없으면 가장 최근 날짜 찾아오기
    StrategyResult findTopByStrategyNameOrderBySignalDateDesc(String strategyName);

    Page<StrategyResult> findByStrategyNameEndingWith(String suffix, Pageable pageable);

    Page<StrategyResult>
    findByStrategyNameAndStrategyNameEndingWith(String strategy, String suffix, Pageable pageable);

    // ✔ 전략명만
    Page<StrategyResult> findByStrategyName(String strategyName, Pageable pageable);

    // ✔ 날짜만
    Page<StrategyResult> findBySignalDate(String signalDate, Pageable pageable);

    // ✔ 전략명 + 날짜
    Page<StrategyResult> findByStrategyNameAndSignalDate(
            String strategyName, String signalDate, Pageable pageable
    );
}