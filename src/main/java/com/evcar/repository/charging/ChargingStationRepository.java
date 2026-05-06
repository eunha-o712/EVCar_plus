package com.evcar.repository.charging;

import com.evcar.domain.charging.ChargingStation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface ChargingStationRepository extends JpaRepository<ChargingStation, String> {

    List<ChargingStation> findByStationIdIn(Collection<String> stationIds);

    List<ChargingStation> findByZcodeOrderByStationNameAsc(String zcode);

    List<ChargingStation> findByZcodeAndZscodeOrderByStationNameAsc(String zcode, String zscode);

    List<ChargingStation> findAllByOrderByZcodeAscZscodeAscStationNameAsc();

    @Query(value = """
            SELECT *
            FROM charging_station
            WHERE lat IS NOT NULL
              AND lng IS NOT NULL
              AND (
                    6371 * ACOS(
                        LEAST(1, GREATEST(-1,
                            COS(RADIANS(:lat)) *
                            COS(RADIANS(lat)) *
                            COS(RADIANS(lng) - RADIANS(:lng)) +
                            SIN(RADIANS(:lat)) *
                            SIN(RADIANS(lat))
                        ))
                    )
                  ) <= :radiusKm
            ORDER BY (
                    6371 * ACOS(
                        LEAST(1, GREATEST(-1,
                            COS(RADIANS(:lat)) *
                            COS(RADIANS(lat)) *
                            COS(RADIANS(lng) - RADIANS(:lng)) +
                            SIN(RADIANS(:lat)) *
                            SIN(RADIANS(lat))
                        ))
                    )
                  ) ASC
            LIMIT 100
            """, nativeQuery = true)
    List<ChargingStation> findByCurrentLocation(
            @Param("lat") double lat,
            @Param("lng") double lng,
            @Param("radiusKm") double radiusKm
    );
}