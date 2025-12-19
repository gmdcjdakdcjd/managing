package com.stock.managing.repository;

import com.stock.managing.domain.KodexEtfHoldingsId;
import com.stock.managing.entity.KodexEtfHoldings;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface KodexEtfHoldingsRepository
        extends JpaRepository<KodexEtfHoldings, KodexEtfHoldingsId> {

    // ETF → 종목 (모달)
    @Query("""
        SELECT h
        FROM KodexEtfHoldings h
        WHERE h.id.etfId = :etfId
        ORDER BY 
            CASE WHEN h.weightRatio IS NULL THEN 0 ELSE 1 END ASC,
            h.weightRatio DESC
    """)
    List<KodexEtfHoldings> findHoldingsOrderByWeightRatioDescNullFirst(
            @Param("etfId") String etfId
    );

    // 종목 → ETF (종목 검색 화면)
    @Query("""
    SELECT h
    FROM KodexEtfHoldings h
    WHERE 
        (:stockCode IS NOT NULL AND h.id.stockCode = :stockCode)
        OR
        (:stockName IS NOT NULL AND h.stockName LIKE %:stockName%)
    ORDER BY 
        CASE WHEN h.weightRatio IS NULL THEN 0 ELSE 1 END ASC,
        h.weightRatio DESC
""")
    List<KodexEtfHoldings> findEtfsByStock(
            @Param("stockCode") String stockCode,
            @Param("stockName") String stockName
    );

}
