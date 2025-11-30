package com.stock.managing.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum USStrategy {

    DAILY_120D_NEW_HIGH_US("120일 신고가 ($)"),
    DAILY_120D_NEW_LOW_US("120일 신저가 ($)"),
    DAILY_BB_LOWER_TOUCH_US("볼린저밴드 하단선 터치값 ($)"),
    DAILY_BB_UPPER_TOUCH_US("볼린저밴드 상단선 터치값 ($)"),
    DAILY_TOUCH_MA60_US("60일선 터치값 ($)"),
    RSI_30_UNHEATED_US("RSI 하단 (30 이하) 진입값"),
    RSI_70_OVERHEATED_US("RSI 상단 (70 이상) 진입값"),
    WEEKLY_52W_NEW_HIGH_US("52주 신고가 ($)"),
    WEEKLY_52W_NEW_LOW_US("52주 신저가 ($)"),
    WEEKLY_TOUCH_MA60_US("주봉 60주선 터치값 ($)");

    private final String captureName;

    public static USStrategy from(String strategyName) {
        for (USStrategy s : values()) {
            if (s.name().equals(strategyName)) return s;
        }
        return null;
    }
}
