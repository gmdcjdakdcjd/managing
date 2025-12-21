package com.stock.managing.repository;

import com.stock.managing.domain.MyEtfItemHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MyEtfHistoryRepository
        extends JpaRepository<MyEtfItemHistoryEntity, Long> {

    List<MyEtfItemHistoryEntity>
    findByUserIdAndEtfNameOrderByDeletedAtDesc(String userId, String etfName);

    List<MyEtfItemHistoryEntity>
    findByUserIdAndEtfNameAndRestoredYnOrderByDeletedAtDesc(
            String userId,
            String etfName,
            String restoredYn
    );


}
