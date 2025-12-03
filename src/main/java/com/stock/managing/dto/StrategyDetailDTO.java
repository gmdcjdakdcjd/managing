package com.stock.managing.dto;

import lombok.*;

import java.sql.Date;
import java.time.LocalDate;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StrategyDetailDTO {

    private String resultId;
    private LocalDate signalDate;
    private String code;
    private String name;
    private String action;
    private Double  price;
    private Double  prevClose;
    private Double  diff;
    private Long  volume;
    private Integer specialValue;
    private Date createdAt;
}
