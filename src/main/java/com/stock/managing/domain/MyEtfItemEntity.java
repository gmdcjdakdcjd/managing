package com.stock.managing.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "my_etf_item", schema = "managing")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyEtfItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", length = 50)
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

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_yn", length = 1)
    private String deletedYn;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    /* =========================
       비즈니스 메서드
       ========================= */

    public void changeQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void softDelete() {
        this.deletedYn = "Y";
        this.deletedAt = LocalDateTime.now();
    }

    public boolean isDeleted() {
        return "Y".equals(this.deletedYn);
    }
}
