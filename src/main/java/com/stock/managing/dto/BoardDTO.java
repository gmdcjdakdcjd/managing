package com.stock.managing.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardDTO {

    private Long bno;
    private String title;
    private String content;
    private String writer;
    private String boardGb; // ✅ 게시판 구분 필드 추가 (11~15)
    private LocalDateTime regDate;
    private LocalDateTime modDate;

    // 첨부파일 이름들
    private List<String> fileNames;

    public String getBoardGbName() {
        switch (boardGb) {
            case "11": return "📈 거래량 전략";
            case "12": return "📊 이동평균선 전략";
            case "13": return "🚀 신고가 / 신저가";
            case "14": return "📉 볼린저 밴드";
            case "15": return "⚡ 급등 / 급락 탐지";
            default: return "기타";
        }
    }
}
