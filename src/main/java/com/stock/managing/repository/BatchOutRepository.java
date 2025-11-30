package com.stock.managing.repository;

import com.stock.managing.domain.BatchOut;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BatchOutRepository extends JpaRepository<BatchOut, Long> {

}