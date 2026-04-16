package com.evcar.dto.vehicle;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleListDto {

    private String vehicleId;
    private String brand;
    private String modelName;
    private String vehicleClass;
    private Integer priceBasic;
    private Integer pricePremium;
    private Integer drivingRange;
    private String imageUrl;
    private String catalogUrl;
    private boolean wished;

    public String getPriceBasicFormatted() {
        if (priceBasic == null) {
            return "-";
        }

        if (pricePremium != null && pricePremium > 0) {
            return String.format("%,d만원 ~ %,d만원", priceBasic, pricePremium);
        }

        return String.format("%,d만원", priceBasic);
    }

    public String getBrandKor() {
        if (brand == null) {
            return "";
        }

        return switch (brand) {
            case "HYUNDAI" -> "현대";
            case "KIA" -> "기아";
            default -> brand;
        };
    }

    public String getVehicleClassKor() {
        if (vehicleClass == null) {
            return "";
        }

        return switch (vehicleClass) {
            case "MINI_SUV" -> "경형SUV";
            case "SMALL_SUV" -> "소형SUV";
            case "MID_SUV" -> "중형SUV";
            case "LARGE_SUV" -> "대형SUV";
            case "MID_SEDAN" -> "세단";
            case "PERFORMANCE" -> "고성능";
            default -> vehicleClass;
        };
    }

    public String getModelNameDisplay() {
        if (modelName == null) {
            return "";
        }

        return modelName.replace("_", " ");
    }
}