package com.stock.managing.repository;

import com.stock.managing.domain.BondDailyYield;
import com.stock.managing.domain.BondDailyYieldId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BondYieldRepository extends JpaRepository<BondDailyYield, BondDailyYieldId> {

    List<BondDailyYield> findByCodeOrderByDateAsc(String code);

}
