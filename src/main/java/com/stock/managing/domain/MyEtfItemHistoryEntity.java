package com.stock.managing.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "my_etf_item_history", schema = "managing")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyEtfItemHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hist_id")
    private Long histId;

    @Column(name = "etf_item_id", nullable = false)
    private Long etfItemId;

    @Column(name = "user_id", nullable = false, length = 50)
    private String userId;

    @Column(name = "code", nullable = false, length = 20)
    private String code;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "etf_name", length = 100)
    private String etfName;

    @Column(name = "etf_description", length = 500)
    private String etfDescription;

    @Column(name = "price_at_add")
    private Double priceAtAdd;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "memo", length = 500)
    private String memo;

    @Column(name = "deleted_at", nullable = false)
    private LocalDateTime deletedAt;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // 🔥 추가
    @Column(name = "restored_yn", nullable = false, length = 1)
    private String restoredYn;

    @Column(name = "restored_at")
    private LocalDateTime restoredAt;

    /* =========================
       삭제 시 백업용 팩토리 메서드
       ========================= */
    public static MyEtfItemHistoryEntity fromEntity(MyEtfItemEntity entity) {
        LocalDateTime now = LocalDateTime.now();

        return MyEtfItemHistoryEntity.builder()
                .etfItemId(entity.getId())
                .userId(entity.getUserId())
                .code(entity.getCode())
                .name(entity.getName())
                .etfName(entity.getEtfName())
                .etfDescription(entity.getEtfDescription())
                .priceAtAdd(entity.getPriceAtAdd())
                .quantity(entity.getQuantity())
                .memo(entity.getMemo())
                .deletedAt(now)
                .createdAt(now)
                .restoredYn("N")      // ✅ 기본값
                .build();
    }

    // 🔥 복구 처리용 메서드 (깔끔)
    public void markRestored() {
        this.restoredYn = "Y";
        this.restoredAt = LocalDateTime.now();
    }
}
