package com.evcar.service.charging;

import com.evcar.dto.charging.ChargingStationDTO;

import java.util.List;

public interface ChargingStationService {

    List<ChargingStationDTO> getStations();

    // 🔥 이거 추가
    void saveFromApi();
}