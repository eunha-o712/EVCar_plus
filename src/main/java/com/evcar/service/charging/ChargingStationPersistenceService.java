package com.evcar.service.charging;

import com.evcar.domain.charging.Charger;
import com.evcar.domain.charging.ChargingStation;
import com.evcar.repository.charging.ChargerRepository;
import com.evcar.repository.charging.ChargingStationRepository;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ChargingStationPersistenceService {

    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    private final ChargingStationRepository chargingStationRepository;
    private final ChargerRepository chargerRepository;
    private final ChargingStationValidator chargingStationValidator;

    @Transactional
    public void savePage(List<JsonNode> items) {
        Set<String> stationIds = new LinkedHashSet<>();
        Map<String, ChargingStation> stationMap = new LinkedHashMap<>();
        List<Charger> chargers = new ArrayList<>();

        for (JsonNode item : items) {
            if (!chargingStationValidator.isValidApiItem(item)) {
                continue;
            }

            ChargingStation station = buildStation(item);
            Charger charger = buildCharger(item, station);

            stationIds.add(station.getStationId());
            stationMap.put(station.getStationId(), station);
            chargers.add(charger);
        }

        if (stationMap.isEmpty()) {
            return;
        }

        chargingStationRepository.saveAll(stationMap.values());
        chargerRepository.deleteByStationIds(stationIds);
        chargerRepository.saveAll(chargers);
    }

    @Transactional
    public void refreshStatusPage(List<JsonNode> items) {
        for (JsonNode item : items) {
            String stationId = getText(item, "statId");
            String chargerId = getText(item, "chgerId");

            if (stationId.isBlank() || chargerId.isBlank()) {
                continue;
            }

            chargerRepository.updateStatusByStationIdAndChargerId(
                    stationId,
                    chargerId,
                    getText(item, "stat"),
                    parseDateTime(getText(item, "statUpdDt"))
            );
        }
    }

    private ChargingStation buildStation(JsonNode item) {
        return ChargingStation.builder()
                .stationId(getText(item, "statId"))
                .stationName(getText(item, "statNm"))
                .address(getText(item, "addr"))
                .lat(getDouble(item, "lat"))
                .lng(getDouble(item, "lng"))
                .useTime(getText(item, "useTime"))
                .zcode(getText(item, "zcode"))
                .zscode(getText(item, "zscode"))
                .operatorName(getText(item, "busiNm"))
                .operatorCall(getText(item, "busiCall"))
                .parkingFree(getText(item, "parkingFree"))
                .note(resolveNote(item))
                .build();
    }

    private Charger buildCharger(JsonNode item, ChargingStation station) {
        return Charger.builder()
                .chargerId(getText(item, "chgerId"))
                .chargerType(getText(item, "chgerType"))
                .powerType(getText(item, "powerType"))
                .status(getText(item, "stat"))
                .statusUpdatedAt(parseDateTime(getText(item, "statUpdDt")))
                .chargingStation(station)
                .build();
    }

    private String resolveNote(JsonNode item) {
        String limitDetail = getText(item, "limitDetail");
        if (!limitDetail.isBlank()) {
            return limitDetail;
        }

        String note = getText(item, "note");
        if (!note.isBlank()) {
            return note;
        }

        return "이용 안내 없음";
    }

    private String getText(JsonNode item, String fieldName) {
        return normalize(item.path(fieldName).asText(""));
    }

    private double getDouble(JsonNode item, String fieldName) {
        String value = getText(item, fieldName);
        if (value.isBlank()) {
            return 0D;
        }

        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return 0D;
        }
    }

    private LocalDateTime parseDateTime(String value) {
        String normalized = normalize(value);
        if (normalized.isBlank()) {
            return null;
        }

        try {
            return LocalDateTime.parse(normalized, DATE_TIME_FORMATTER);
        } catch (Exception e) {
            return null;
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