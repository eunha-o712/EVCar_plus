package com.evcar.repository.charging;

import com.evcar.domain.charging.ChargingStation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface ChargingStationRepository extends JpaRepository<ChargingStation, String> {

    List<ChargingStation> findByStationIdIn(Collection<String> stationIds);

    List<ChargingStation> findByZcodeOrderByStationNameAsc(String zcode);

    List<ChargingStation> findByZcodeAndZscodeOrderByStationNameAsc(String zcode, String zscode);

    List<ChargingStation> findAllByOrderByZcodeAscZscodeAscStationNameAsc();
}