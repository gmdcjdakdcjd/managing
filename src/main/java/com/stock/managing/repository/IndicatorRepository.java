package com.stock.managing.repository;

import com.stock.managing.domain.MarketIndicator;
import com.stock.managing.dto.MarketIndicatorDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface IndicatorRepository extends JpaRepository<MarketIndicator, Long> {

    // 특정 코드(KOSPI, USD 등) 날짜 순으로 조회
    List<MarketIndicator> findByCodeOrderByDateAsc(String code);
}