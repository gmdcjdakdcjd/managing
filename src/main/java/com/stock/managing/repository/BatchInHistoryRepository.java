package com.stock.managing.repository;

import com.stock.managing.domain.BatchInHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BatchInHistoryRepository extends JpaRepository<BatchInHistory, Long> {
    List<BatchInHistory> findAllByOrderByExecStartTimeDesc();
}
