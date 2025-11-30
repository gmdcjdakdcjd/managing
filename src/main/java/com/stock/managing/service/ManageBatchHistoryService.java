package com.stock.managing.service;

import com.stock.managing.domain.BatchInHistory;
import com.stock.managing.domain.BatchOutHistory;
import com.stock.managing.dto.BatchInHistoryDTO;
import com.stock.managing.dto.BatchOutHistoryDTO;
import com.stock.managing.dto.BatchDateGroupDTO;
import com.stock.managing.repository.BatchInHistoryRepository;
import com.stock.managing.repository.BatchOutHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ManageBatchHistoryService {

    private final BatchInHistoryRepository inRepo;
    private final BatchOutHistoryRepository outRepo;

    /**
     * 최종적으로 Controller에서 사용할 그룹 데이터
     */
    public List<BatchDateGroupDTO> getGroupedHistory() {

        // 1) IN + OUT 이력 모두 가져오기
        List<Object> all = new ArrayList<>();
        all.addAll(
                inRepo.findAllByOrderByExecStartTimeDesc()
                        .stream().map(this::toInDTO).toList()
        );

        all.addAll(
                outRepo.findAllByOrderByExecStartTimeDesc()
                        .stream().map(this::toOutDTO).toList()
        );

        // 2) execDate 기준으로 그룹화
        Map<LocalDate, List<Object>> grouped =
                all.stream().collect(Collectors.groupingBy(item -> {
                    if (item instanceof BatchInHistoryDTO dto) {
                        return dto.getExecDate();
                    } else if (item instanceof BatchOutHistoryDTO dto) {
                        return dto.getExecDate();
                    }
                    return null;
                }));

        // 3) DTO로 변환해서 정렬 (날짜 내림차순)
        return grouped.entrySet().stream()
                .sorted(Map.Entry.<LocalDate, List<Object>>comparingByKey().reversed())
                .map(e -> new BatchDateGroupDTO(
                        e.getKey(),
                        e.getKey().toString().replace("-", ""), // dateKey 생성
                        e.getValue()
                ))
                .toList();
    }


    private BatchInHistoryDTO toInDTO(BatchInHistory h) {
        return BatchInHistoryDTO.builder()
                .histId(h.getHistId())
                .jobId(h.getJobId())
                .jobName(h.getJobName())
                .jobInfo(h.getJobInfo())
                .execStartTime(h.getExecStartTime())
                .execEndTime(h.getExecEndTime())
                .execStatus(h.getExecStatus())
                .execMessage(h.getExecMessage())
                .execDate(h.getExecDate())
                .durationMs(h.getDurationMs())
                .type("IN")
                .build();
    }

    private BatchOutHistoryDTO toOutDTO(BatchOutHistory h) {
        return BatchOutHistoryDTO.builder()
                .histId(h.getHistId())
                .jobId(h.getJobId())
                .jobName(h.getJobName())
                .jobInfo(h.getJobInfo())
                .execStartTime(h.getExecStartTime())
                .execEndTime(h.getExecEndTime())
                .execStatus(h.getExecStatus())
                .execMessage(h.getExecMessage())
                .execDate(h.getExecDate())
                .durationMs(h.getDurationMs())
                .type("OUT")
                .build();
    }
}
