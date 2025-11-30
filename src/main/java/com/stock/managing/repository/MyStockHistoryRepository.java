package com.stock.managing.repository;

import com.stock.managing.domain.MyStockHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MyStockHistoryRepository extends JpaRepository<MyStockHistory, Long> {

    // 원본 my_stock_id로 히스토리 조회 (최근순)
    List<MyStockHistory> findByMyStockIdOrderByCreatedAtDesc(Long myStockId);

    // 유저 전체 히스토리 조회
    List<MyStockHistory> findByUserIdOrderByCreatedAtDesc(String userId);
}
