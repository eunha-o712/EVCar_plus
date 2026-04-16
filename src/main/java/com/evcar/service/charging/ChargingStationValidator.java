package com.evcar.service.charging;

import com.evcar.domain.charging.ChargingStation;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

@Component
public class ChargingStationValidator {

    private static final double KOREA_MIN_LAT = 33.0;
    private static final double KOREA_MAX_LAT = 39.0;
    private static final double KOREA_MIN_LNG = 124.0;
    private static final double KOREA_MAX_LNG = 132.0;

    public boolean isValidApiItem(JsonNode item) {
        String stationId = normalize(item.path("statId").asText(""));
        String stationName = normalize(item.path("statNm").asText(""));
        String address = normalize(item.path("addr").asText(""));
        String zcode = normalize(item.path("zcode").asText(""));
        String zscode = normalize(item.path("zscode").asText(""));
        String chargerId = normalize(item.path("chgerId").asText(""));
        String chargerType = normalize(item.path("chgerType").asText(""));
        double lat = parseDouble(item.path("lat").asText(""));
        double lng = parseDouble(item.path("lng").asText(""));

        return !stationId.isBlank()
                && !stationName.isBlank()
                && !address.isBlank()
                && !zcode.isBlank()
                && !zscode.isBlank()
                && !chargerId.isBlank()
                && !chargerType.isBlank()
                && isValidCoordinate(lat, lng);
    }

    public boolean isValidStation(ChargingStation station) {
        if (station == null) {
            return false;
        }

        return !normalize(station.getStationId()).isBlank()
                && !normalize(station.getStationName()).isBlank()
                && !normalize(station.getAddress()).isBlank()
                && !normalize(station.getZcode()).isBlank()
                && !normalize(station.getZscode()).isBlank()
                && station.getLat() != null
                && station.getLng() != null
                && isValidCoordinate(station.getLat(), station.getLng());
    }

    private boolean isValidCoordinate(double lat, double lng) {
        return lat >= KOREA_MIN_LAT
                && lat <= KOREA_MAX_LAT
                && lng >= KOREA_MIN_LNG
                && lng <= KOREA_MAX_LNG;
    }

    private double parseDouble(String value) {
        String normalized = normalize(value);
        if (normalized.isBlank()) {
            return 0D;
        }

        try {
            return Double.parseDouble(normalized);
        } catch (NumberFormatException e) {
            return 0D;
        }
    }

    private String normalize(String value) {
        if (value == null) {
            return "";
        }

        String normalized = value.trim();
        if ("null".equalsIgnoreCase(normalized)) {
            return "";
        }

        return normalized;
    }
}