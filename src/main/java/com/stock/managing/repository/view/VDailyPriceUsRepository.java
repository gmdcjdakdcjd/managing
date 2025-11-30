package com.stock.managing.repository.view;

import com.stock.managing.domain.view.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VDailyPriceUsRepository extends JpaRepository<VDailyPriceUs, Long> {

    @Query(value = """
                        SELECT * 
                        FROM daily_price_us
                        WHERE code = :code
                        ORDER BY date DESC
                    """, nativeQuery = true)
    List<VDailyPriceUs> findAllByCodeOrderByDateDesc(@Param("code") String code);
}
