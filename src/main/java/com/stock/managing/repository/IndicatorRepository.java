package com.stock.managing.repository;


import com.stock.managing.domain.DailyPriceIndicator;
import com.stock.managing.dto.MarketIndicatorDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface IndicatorRepository extends JpaRepository<DailyPriceIndicator, Long> {

    // 특정 코드(KOSPI, USD 등) 날짜 순으로 조회
    List<DailyPriceIndicator> findByCodeOrderByDateAsc(String code);

    @Query(
            value = """
        select close
        from daily_price_indicator
        where code = :code
        order by last_update desc
        limit 1
      """,
            nativeQuery = true
    )
    Double findLatestValue(@Param("code") String code);
}