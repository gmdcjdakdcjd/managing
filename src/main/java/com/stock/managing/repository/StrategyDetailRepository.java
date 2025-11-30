package com.stock.managing.repository;

import com.stock.managing.domain.StrategyDetail;
import com.stock.managing.domain.StrategyDetailId;
import com.stock.managing.dto.StrategyDetailDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StrategyDetailRepository
        extends JpaRepository<StrategyDetail, StrategyDetailId> {

    List<StrategyDetail> findByActionAndSignalDateOrderBySpecialValueAsc
            ( String action, String signalDate);

    List<StrategyDetail> findByActionOrderBySignalDateDesc(String action);

    @Query("""
        SELECT new com.stock.managing.dto.StrategyDetailDTO(
            d.resultId,
            d.signalDate,
            d.code,
            d.name,
            d.action,
            d.price,
            d.prevClose,
            d.diff,
            d.volume,
            d.specialValue,
            d.createdAt
        )
        FROM StrategyDetail d
        WHERE d.name LIKE %:keyword%
           OR d.code LIKE %:keyword%
           OR d.action LIKE %:keyword%
        ORDER BY d.signalDate DESC, d.specialValue ASC
    """)
    List<StrategyDetailDTO> findByKeyword(@Param("keyword") String keyword);
}



