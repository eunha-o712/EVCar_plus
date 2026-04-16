package com.evcar.repository.charging;

import com.evcar.domain.charging.Charger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface ChargerRepository extends JpaRepository<Charger, Long> {

    List<Charger> findByChargingStation_StationIdOrderByChargerIdAsc(String stationId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
        delete from Charger c
        where c.chargingStation.stationId in :stationIds
    """)
    void deleteByStationIds(@Param("stationIds") Collection<String> stationIds);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
        update Charger c
           set c.status = :status,
               c.statusUpdatedAt = :statusUpdatedAt
         where c.chargingStation.stationId = :stationId
           and c.chargerId = :chargerId
    """)
    int updateStatusByStationIdAndChargerId(
            @Param("stationId") String stationId,
            @Param("chargerId") String chargerId,
            @Param("status") String status,
            @Param("statusUpdatedAt") LocalDateTime statusUpdatedAt
    );
}