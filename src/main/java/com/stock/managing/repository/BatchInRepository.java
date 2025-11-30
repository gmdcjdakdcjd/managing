package com.stock.managing.repository;


import com.stock.managing.domain.BatchIn;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BatchInRepository extends JpaRepository<BatchIn, Long> {
}