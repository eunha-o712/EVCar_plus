package com.evcar.service.charging;

import com.evcar.domain.charging.ChargingStation;
import com.evcar.dto.charging.ChargingRegionResponseDto;
import com.evcar.dto.charging.ChargingStationResponseDto;
import com.evcar.dto.charging.SigunguResponseDto;
import com.evcar.repository.charging.ChargingStationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChargingStationServiceImpl implements ChargingStationService {

    private final ChargingStationRepository chargingStationRepository;
    private final ChargingStationValidator chargingStationValidator;

    @Override
    public List<ChargingRegionResponseDto> getAllRegions() {
        List<ChargingStation> stations = chargingStationRepository.findAllByOrderByZcodeAscZscodeAscStationNameAsc();

        Map<String, RegionAccumulator> regionMap = new LinkedHashMap<>();

        for (ChargingStation station : stations) {
            if (!chargingStationValidator.isValidStation(station)) {
                continue;
            }

            String zcode = normalize(station.getZcode());
            String zscode = normalize(station.getZscode());
            String sido = extractSido(station.getAddress());
            String sigungu = extractSigungu(station.getAddress());

            if (zcode.isBlank() || zscode.isBlank() || sido.isBlank() || sigungu.isBlank()) {
                continue;
            }

            RegionAccumulator accumulator = regionMap.computeIfAbsent(
                    zcode,
                    key -> new RegionAccumulator(sido)
            );

            accumulator.sigunguMap.putIfAbsent(
                    zscode,
                    SigunguResponseDto.builder()
                            .zscode(zscode)
                            .sigungu(sigungu)
                            .build()
            );
        }

        List<ChargingRegionResponseDto> result = new ArrayList<>();
        for (Map.Entry<String, RegionAccumulator> entry : regionMap.entrySet()) {
            result.add(ChargingRegionResponseDto.builder()
                    .zcode(entry.getKey())
                    .sido(entry.getValue().sido)
                    .sigunguList(new ArrayList<>(entry.getValue().sigunguMap.values()))
                    .build());
        }

        return result;
    }

    @Override
    public List<ChargingStationResponseDto> getStations(String zcode, String zscode) {
        String normalizedZcode = normalize(zcode);
        String normalizedZscode = normalize(zscode);

        if (normalizedZcode.isBlank()) {
            return List.of();
        }

        List<ChargingStation> stations = normalizedZscode.isBlank()
                ? chargingStationRepository.findByZcodeOrderByStationNameAsc(normalizedZcode)
                : chargingStationRepository.findByZcodeAndZscodeOrderByStationNameAsc(normalizedZcode, normalizedZscode);

        return stations.stream()
                .filter(chargingStationValidator::isValidStation)
                .map(this::toResponseDto)
                .toList();
    }

    private ChargingStationResponseDto toResponseDto(ChargingStation station) {
        return ChargingStationResponseDto.builder()
                .stationId(station.getStationId())
                .stationName(station.getStationName())
                .address(station.getAddress())
                .lat(station.getLat())
                .lng(station.getLng())
                .operatorName(station.getOperatorName())
                .operatorCall(station.getOperatorCall())
                .useTime(station.getUseTime())
                .parkingFree(toParkingFreeText(station.getParkingFree()))
                .note(toNoteText(station.getNote()))
                .build();
    }

    private String extractSido(String address) {
        String[] parts = splitAddress(address);
        return parts.length > 0 ? parts[0] : "";
    }

    private String extractSigungu(String address) {
        String[] parts = splitAddress(address);
        return parts.length > 1 ? parts[1] : "";
    }

    private String[] splitAddress(String address) {
        String normalized = normalize(address);
        return normalized.isBlank() ? new String[0] : normalized.split("\\s+");
    }

    private String toParkingFreeText(String parkingFree) {
        return "Y".equalsIgnoreCase(normalize(parkingFree)) ? "무료" : "유료";
    }

    private String toNoteText(String note) {
        String normalized = normalize(note);
        return normalized.isBlank() ? "이용 안내 없음" : normalized;
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

    private static final class RegionAccumulator {
        private final String sido;
        private final Map<String, SigunguResponseDto> sigunguMap = new LinkedHashMap<>();

        private RegionAccumulator(String sido) {
            this.sido = sido;
        }
    }
}