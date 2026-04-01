package com.evcar.repository.charging;

import com.evcar.domain.charging.ChargingStation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChargingStationRepository extends JpaRepository<ChargingStation, Long> {

    // 🔥 핵심 (주소 기반 검색)
    List<ChargingStation> findByAddressContaining(String keyword);
}