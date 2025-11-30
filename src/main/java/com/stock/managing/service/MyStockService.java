package com.stock.managing.service;

import com.stock.managing.domain.MyStock;
import com.stock.managing.domain.MyStockHistory;
import com.stock.managing.dto.MyStockDTO;
import com.stock.managing.dto.StockDTO;
import com.stock.managing.repository.MyStockHistoryRepository;
import com.stock.managing.repository.MyStockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MyStockService {

    private final MyStockRepository myStockRepository;
    private final MyStockHistoryRepository myStockHistoryRepository;
    private final StockViewService stockViewService;

    public void addBatch(String userId, List<MyStockDTO> list) {

        for (MyStockDTO dto : list) {

            dto.setUserId(userId);

            // 가격 Double 변환 (혹시 프론트에서 문자열 올 경우 대비)
            Double price = dto.getPriceAtAdd();
            if (price != null) {
                dto.setTargetPrice5(Math.round(price * 1.05 * 100) / 100.0);
                dto.setTargetPrice10(Math.round(price * 1.10 * 100) / 100.0);
            }

            // DB 저장
            MyStock saved = myStockRepository.save(dto.toEntity());

            // HISTORY 저장
            myStockHistoryRepository.save(
                    MyStockHistory.builder()
                            .myStockId(saved.getId())
                            .userId(saved.getUserId())
                            .code(saved.getCode())
                            .name(saved.getName())
                            .action("ADD")
                            .strategyName(saved.getStrategyName())
                            .specialValue(saved.getSpecialValue())
                            .priceAtAdd(saved.getPriceAtAdd())
                            .targetPrice5(saved.getTargetPrice5())
                            .targetPrice10(saved.getTargetPrice10())
                            .memo(saved.getMemo())
                            .note("전략 상세 페이지에서 추가됨")
                            .build()
            );
        }
    }

    public List<MyStockDTO> getMyStockList(String userId) {

        List<MyStockDTO> list = myStockRepository
                .findByUserIdAndDeletedYn(userId, "N")
                .stream()
                .map(MyStockDTO::fromEntity)
                .toList();

        for (MyStockDTO dto : list) {

            // 종목 코드로 최신 시세 조회
            StockDTO stock = stockViewService.getStockInfo(null, dto.getCode());

            if (stock != null && stock.getPriceList() != null && !stock.getPriceList().isEmpty()) {
                double currentPrice = stock.getPriceList().get(0).getClose();
                dto.setCurrentPrice(currentPrice);
            } else {
                dto.setCurrentPrice(null);
            }
        }

        return list;
    }


    public void delete(Long id, String userId) {
        MyStock stock = myStockRepository.findById(id).orElseThrow();
        stock.setDeletedYn("Y");
        stock.setDeletedAt(LocalDateTime.now());
        myStockRepository.save(stock);

        myStockHistoryRepository.save(
                MyStockHistory.builder()
                        .myStockId(id)
                        .userId(userId)
                        .code(stock.getCode())
                        .name(stock.getName())
                        .action("DELETE")
                        .note("내 종목 리스트에서 삭제됨")
                        .build()
        );
    }

    public List<MyStockDTO> getDeletedList(String userId) {
        return myStockRepository
                .findByUserIdAndDeletedYn(userId, "Y")
                .stream()
                .map(MyStockDTO::fromEntity)
                .toList();
    }


    public void restore(Long id, String userId) {
        MyStock stock = myStockRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID not found"));

        if (!stock.getUserId().equals(userId)) {
            throw new SecurityException("본인 소유만 복구 가능");
        }

        stock.setDeletedYn("N");
        stock.setDeletedAt(null);

        myStockRepository.save(stock);

        myStockHistoryRepository.save(
                MyStockHistory.builder()
                        .myStockId(stock.getId())
                        .userId(stock.getUserId())
                        .code(stock.getCode())
                        .name(stock.getName())
                        .action("RESTORE")
                        .note("관심종목 복구")
                        .build()
        );
    }


}
