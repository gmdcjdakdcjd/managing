package com.stock.managing.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum KRStrategy {

    DAILY_120D_NEW_HIGH_KR("120일 신고가 (원)"),
    DAILY_120D_NEW_LOW_KR("120일 신저가 (원)"),
    DAILY_BB_LOWER_TOUCH_KR("볼린저밴드 하단선 터치값 (원)"),
    DAILY_BB_UPPER_TOUCH_KR("볼린저밴드 상단선 터치값 (원)"),
    DAILY_TOUCH_MA60_KR("60일선 터치값 (원)"),
    RSI_30_UNHEATED_KR("RSI 하단 (30 이하) 진입값"),
    RSI_70_OVERHEATED_KR("RSI 상단 (70 이상) 진입값"),
    WEEKLY_52W_NEW_HIGH_KR("52주 신고가 (원)"),
    WEEKLY_52W_NEW_LOW_KR("52주 신저가 (원)"),
    WEEKLY_TOUCH_MA60_KR("주봉 60주선 터치값 (원)");

    private final String captureName;

    public static KRStrategy from(String strategyName) {
        for (KRStrategy s : values()) {
            if (s.name().equals(strategyName)) return s;
        }
        return null;
    }
}
