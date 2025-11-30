package com.stock.managing.repository;

import com.stock.managing.domain.BatchOutHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BatchOutHistoryRepository extends JpaRepository<BatchOutHistory, Long> {
    List<BatchOutHistory> findAllByOrderByExecStartTimeDesc();
}