package com.evcar.dto.admin;

import com.evcar.common.enums.VehicleModelType;
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
        return VehicleModelType.toLabel(modelName);
    }

    public String getVehicleClassLabel() {
        if (vehicleClass == null) {
            return "-";
        }

        return switch (vehicleClass.toUpperCase()) {
            case "MINI_SUV" -> "경형";
            case "SMALL_SUV" -> "소형";
            case "MID_SUV" -> "준중형";
            case "LARGE_SUV" -> "대형";
            case "MID_SEDAN" -> "세단";
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