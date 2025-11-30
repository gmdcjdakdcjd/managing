package com.stock.managing.repository;

import com.stock.managing.domain.MyStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MyStockRepository extends JpaRepository<MyStock, Long> {

    // 특정 user의 종목 리스트 조회
    List<MyStock> findByUserIdAndDeletedYn(String userId, String deletedYn);


    // 이미 관심종목에 존재하는지 체크 (중복 체크용)
    boolean existsByUserIdAndCode(String userId, String code);

    // 특정 user + 특정 종목 가져오기 (상세 페이지용)
    MyStock findByUserIdAndCode(String userId, String code);
}
