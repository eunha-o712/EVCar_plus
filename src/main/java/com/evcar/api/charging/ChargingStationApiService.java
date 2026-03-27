package com.evcar.api.charging;

import com.evcar.dto.charging.ChargingStationDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChargingStationApiService {

    public List<ChargingStationDTO> fetchStations() {

        return List.of(
                ChargingStationDTO.builder()
                        .stationName("테스트 충전소")
                        .latitude(37.5665)
                        .longitude(126.9780)
                        .address("서울")
                        .build()
        );
    }
}