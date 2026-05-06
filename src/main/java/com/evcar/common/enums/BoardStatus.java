package com.evcar.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum BoardStatus {

    WAITING("WAITING", "대기", "waiting"),
    PENDING("PENDING", "대기", "waiting"),

    IN_PROGRESS("IN_PROGRESS", "진행", "progress"),
    PROGRESS("PROGRESS", "진행", "progress"),

    COMPLETED("COMPLETED", "완료", "success"),
    REPLIED("REPLIED", "완료", "success"),
    DONE("DONE", "완료", "success"),

    CANCELED("CANCELED", "취소", "cancel"),
    CANCEL("CANCEL", "취소", "cancel"),

    CLOSED("CLOSED", "종료", "danger"),

    UNKNOWN("UNKNOWN", "대기", "waiting");

    private final String code;
    private final String label;
    private final String cssClass;

    public static BoardStatus from(String code) {
        if (code == null || code.isBlank()) {
            return UNKNOWN;
        }

        return Arrays.stream(values())
                .filter(status -> status.code.equalsIgnoreCase(code.trim()))
                .findFirst()
                .orElse(UNKNOWN);
    }
}