package com.evcar.common.enums;

import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum VehicleModelType {

    IONIQ_5("IONIQ_5", "IONIQ 5"),
    IONIQ_5_N("IONIQ_5_N", "IONIQ 5 N"),
    IONIQ_6("IONIQ_6", "IONIQ 6"),
    IONIQ_9("IONIQ_9", "IONIQ 9"),

    CASPER_EV("CASPER_EV", "CASPER EV"),
    RAY_EV("RAY_EV", "RAY EV"),

    EV3("EV3", "EV3"),
    EV6("EV6", "EV6"),
    EV9("EV9", "EV9"),

    KONA_ELECTRIC("KONA_ELECTRIC", "KONA ELECTRIC"),
    NIRO_EV("NIRO_EV", "NIRO EV");

    private final String code;
    private final String label;

    public static String toLabel(String code) {

        if (code == null || code.isBlank()) {
            return "-";
        }

        return Arrays.stream(values())
                .filter(value ->
                        value.code.equalsIgnoreCase(code)
                )
                .findFirst()
                .map(VehicleModelType::getLabel)
                .orElse(code.replace("_", " "));
    }
}