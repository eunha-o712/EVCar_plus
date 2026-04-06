package com.evcar.common.enums;

import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum VehicleModelType {

    IONIQ_5("IONIQ_5", "아이오닉 5"),
    IONIQ_5_N("IONIQ_5_N", "아이오닉 5 N"),
    IONIQ_6("IONIQ_6", "아이오닉 6"),
    IONIQ_9("IONIQ_9", "아이오닉 9"),
    CASPER_EV("CASPER_EV", "캐스퍼 일렉트릭"),
    RAY_EV("RAY_EV", "레이 EV"),
    EV3("EV3", "EV3"),
    EV6("EV6", "EV6"),
    EV9("EV9", "EV9"),
    KONA_ELECTRIC("KONA_ELECTRIC", "코나 일렉트릭"),
    NIRO_EV("NIRO_EV", "니로 EV");

    private final String code;
    private final String label;

    public static String toLabel(String code) {
        if (code == null || code.isBlank()) {
            return "-";
        }

        return Arrays.stream(values())
                .filter(value -> value.code.equalsIgnoreCase(code))
                .findFirst()
                .map(VehicleModelType::getLabel)
                .orElse(code);
    }
}