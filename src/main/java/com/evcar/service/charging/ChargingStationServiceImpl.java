package com.evcar.service.charging;

import com.evcar.api.charging.ChargingStationApiService;
import com.evcar.domain.charging.ChargingStation;
import com.evcar.dto.charging.ChargingStationDTO;
import com.evcar.repository.charging.ChargingStationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChargingStationServiceImpl implements ChargingStationService {

    private final ChargingStationRepository repository;
    private final ChargingStationApiService apiService;

    // 공공데이터 → DB 저장
    @Transactional
    @Override
    public void saveFromApi() {

        List<ChargingStationDTO> list = apiService.fetchStations();

        list.forEach(dto -> {
            ChargingStation entity = ChargingStation.builder()
                    .stationName(dto.getStationName())
                    .lat(dto.getLatitude())
                    .lng(dto.getLongitude())
                    .address(dto.getAddress())
                    .build();

            repository.save(entity);
        });
    }

    // DB → 프론트로 데이터 반환 (이게 핵심)
    @Override
    public List<ChargingStationDTO> getStations() {

        return repository.findAll().stream()
                .map(entity -> ChargingStationDTO.builder()
                        .stationId(entity.getStationId())
                        .stationName(entity.getStationName())
                        .latitude(entity.getLat())
                        .longitude(entity.getLng())
                        .address(entity.getAddress())
                        .build())
                .toList();
    }
}