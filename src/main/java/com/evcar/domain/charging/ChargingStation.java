package com.evcar.domain.charging;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "charging_station")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ChargingStation {

    @Id
    @Column(name = "station_id", length = 50, nullable = false)
    private String stationId;

    @Column(name = "station_name", length = 100, nullable = false)
    private String stationName;

    @Column(name = "address", length = 255, nullable = false)
    private String address;

    @Column(name = "lat", nullable = false)
    private Double lat;

    @Column(name = "lng", nullable = false)
    private Double lng;

    @Column(name = "use_time", length = 100)
    private String useTime;

    @Column(name = "zcode", length = 20)
    private String zcode;

    @Column(name = "zscode", length = 20)
    private String zscode;

    @Column(name = "operator_name", length = 100)
    private String operatorName;

    @Column(name = "operator_call", length = 20)
    private String operatorCall;

    @Column(name = "parking_free", length = 20)
    private String parkingFree;

    @Column(name = "note", columnDefinition = "TEXT")
    private String note;

    @OneToMany(mappedBy = "chargingStation", fetch = FetchType.LAZY)
    private List<Charger> chargers;
}