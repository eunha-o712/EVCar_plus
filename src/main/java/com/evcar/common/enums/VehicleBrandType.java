package com.evcar.common.enums;

import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum VehicleBrandType {

    HYUNDAI("HYUNDAI", "HYUNDAI"),
    KIA("KIA", "KIA");

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
                .map(VehicleBrandType::getLabel)
                .orElse(code);
    }

    public static boolean isValid(String code) {

        if (code == null || code.isBlank()) {
            return false;
        }

        return Arrays.stream(values())
                .anyMatch(value ->
                        value.code.equalsIgnoreCase(code)
                );
    }

    public static VehicleBrandType fromCode(String code) {

        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("브랜드 코드가 비어있습니다.");
        }

        return Arrays.stream(values())
                .filter(value ->
                        value.code.equalsIgnoreCase(code)
                )
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException("존재하지 않는 브랜드 코드입니다. : " + code)
                );
    }
}
