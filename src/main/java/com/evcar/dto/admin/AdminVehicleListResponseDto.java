package com.evcar.dto.admin;

import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminVehicleListResponseDto {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private String vehicleId;
    private String brand;
    private String modelName;
    private String vehicleClass;
    private String vehicleStatus;
    private Integer priceBasic;
    private String imageUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public String getVehicleStatusLabel() {
        if ("SOLD_OUT".equalsIgnoreCase(vehicleStatus)) {
            return "품절";
        }
        return "판매중";
    }

    public String getModelNameLabel() {
        if (modelName == null) {
            return "-";
        }

        return switch (modelName.toUpperCase()) {
            case "IONIQ_5" -> "아이오닉 5";
            case "IONIQ_5_N" -> "아이오닉 5 N";
            case "IONIQ_6" -> "아이오닉 6";
            case "IONIQ_9" -> "아이오닉 9";
            case "CASPER_EV" -> "캐스퍼 일렉트릭";
            case "EV3" -> "EV3";
            case "EV6" -> "EV6";
            case "EV9" -> "EV9";
            default -> modelName;
        };
    }

    public String getVehicleClassLabel() {
        if (vehicleClass == null) {
            return "-";
        }

        return switch (vehicleClass.toUpperCase()) {
            case "MINI_SUV" -> "소형 SUV";
            case "SMALL_SUV" -> "준중형 SUV";
            case "MID_SUV" -> "중형 SUV";
            case "LARGE_SUV" -> "대형 SUV";
            case "MID_SEDAN" -> "중형 세단";
            case "PERFORMANCE" -> "고성능";
            default -> vehicleClass;
        };
    }

    public String getPriceBasicFormatted() {
        if (priceBasic == null) {
            return "-";
        }
        return NumberFormat.getNumberInstance(Locale.KOREA).format(priceBasic) + "만원";
    }

    public String getCreatedAtFormatted() {
        if (createdAt == null) {
            return "-";
        }
        return createdAt.format(DATE_TIME_FORMATTER);
    }

    public String getUpdatedAtFormatted() {
        if (updatedAt == null) {
            return "-";
        }
        return updatedAt.format(DATE_TIME_FORMATTER);
    }
}