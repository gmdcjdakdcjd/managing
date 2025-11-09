package com.stock.managing.repository;


import com.stock.managing.repository.view.VwStrategySignalInfo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SignalInfoRepository extends JpaRepository<VwStrategySignalInfo, Long> {

    @Query(value = """
    SELECT sig.name, sig.code, sig.strategy_name, sig.signal_date, sig.result_id
    FROM vw_strategy_signal_info sig
    WHERE LOWER(sig.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
       OR LOWER(sig.code) LIKE LOWER(CONCAT('%', :keyword, '%'))
    ORDER BY sig.signal_date DESC
""", nativeQuery = true)
    List<Object[]> findAllRawByKeyword(@Param("keyword") String keyword);


}
