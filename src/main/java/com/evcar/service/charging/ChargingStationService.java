package com.evcar.service.charging;

import com.evcar.dto.charging.ChargingRegionResponseDto;
import com.evcar.dto.charging.ChargingStationResponseDto;

import java.util.List;

public interface ChargingStationService {

    List<ChargingRegionResponseDto> getAllRegions();

    List<ChargingStationResponseDto> getStations(String zcode, String zscode);
}