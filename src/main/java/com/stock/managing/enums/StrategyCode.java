package com.stock.managing.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StrategyCode {

    // ============================
    // KR 전략
    // ============================
    DAILY_120D_NEW_HIGH_KR("DAILY_120D_NEW_HIGH_KR", "120일 신고가", "KR"),
    DAILY_120D_NEW_LOW_KR("DAILY_120D_NEW_LOW_KR", "120일 신저가", "KR"),
    DAILY_BB_LOWER_TOUCH_KR("DAILY_BB_LOWER_TOUCH_KR", "볼밴 하단 터치", "KR"),
    DAILY_BB_UPPER_TOUCH_KR("DAILY_BB_UPPER_TOUCH_KR", "볼밴 상단 터치", "KR"),
    DAILY_TOUCH_MA60_KR("DAILY_TOUCH_MA60_KR", "60일선 터치", "KR"),
    DAILY_DROP_SPIKE_KR("DAILY_DROP_SPIKE_KR", "급락 스파이크", "KR"),
    DAILY_RISE_SPIKE_KR("DAILY_RISE_SPIKE_KR", "급등 스파이크", "KR"),
    DAILY_TOP20_VOLUME_KR("DAILY_TOP20_VOLUME_KR", "상위 20 거래량", "KR"),
    RSI_30_UNHEATED_KR("RSI_30_UNHEATED_KR", "RSI 비과열(30 이하)", "KR"),
    RSI_70_OVERHEATED_KR("RSI_70_OVERHEATED_KR", "RSI 과열(70 이상)", "KR"),
    WEEKLY_52W_NEW_HIGH_KR("WEEKLY_52W_NEW_HIGH_KR", "주봉 52주 신고가", "KR"),
    WEEKLY_52W_NEW_LOW_KR("WEEKLY_52W_NEW_LOW_KR", "주봉 52주 신저가", "KR"),
    WEEKLY_TOUCH_MA60_KR("WEEKLY_TOUCH_MA60_KR", "주봉 60주선 터치", "KR"),

    // ============================
    // US 전략
    // ============================
    DAILY_120D_NEW_HIGH_US("DAILY_120D_NEW_HIGH_US", "120일 신고가", "US"),
    DAILY_120D_NEW_LOW_US("DAILY_120D_NEW_LOW_US", "120일 신저가", "US"),
    DAILY_BB_LOWER_TOUCH_US("DAILY_BB_LOWER_TOUCH_US", "볼밴 하단 터치", "US"),
    DAILY_BB_UPPER_TOUCH_US("DAILY_BB_UPPER_TOUCH_US", "볼밴 상단 터치", "US"),
    DAILY_TOUCH_MA60_US("DAILY_TOUCH_MA60_US", "60일선 터치", "US"),
    DAILY_DROP_SPIKE_US("DAILY_DROP_SPIKE_US", "급락 스파이크", "US"),
    DAILY_RISE_SPIKE_US("DAILY_RISE_SPIKE_US", "급등 스파이크", "US"),
    DAILY_TOP20_VOLUME_US("DAILY_TOP20_VOLUME_US", "상위 20 거래량", "US"),
    RSI_30_UNHEATED_US("RSI_30_UNHEATED_US", "RSI 비과열(30 이하)", "US"),
    RSI_70_OVERHEATED_US("RSI_70_OVERHEATED_US", "RSI 과열(70 이상)", "US"),
    WEEKLY_52W_NEW_HIGH_US("WEEKLY_52W_NEW_HIGH_US", "주봉 52주 신고가", "US"),
    WEEKLY_52W_NEW_LOW_US("WEEKLY_52W_NEW_LOW_US", "주봉 52주 신저가", "US"),
    WEEKLY_TOUCH_MA60_US("WEEKLY_TOUCH_MA60_US", "주봉 60주선 터치", "US");

    private final String code;   // DB 저장용
    private final String label;  // 화면 표시용 한국어
    private final String market; // KR or US

    // =============== 헬퍼 메서드 ===============
    public static StrategyCode findByCode(String code) {
        for (StrategyCode sc : values()) {
            if (sc.code.equals(code)) return sc;
        }
        return null;
    }
}
