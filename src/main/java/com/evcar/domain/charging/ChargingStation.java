package com.evcar.domain.charging;

import jakarta.persistence.*;
import lombok.*;

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

    @Column(name = "station_name", length = 100)
    private String stationName;

    @Column(name = "address", length = 255)
    private String address;

    @Column(name = "lat")
    private Double lat;

    @Column(name = "lng")
    private Double lng;

    @Column(name = "use_time", length = 100)
    private String useTime;

    // 🔥 핵심 (정상)
    @Column(name = "zcode", length = 20)
    private String zcode;

    @Column(name = "sido", length = 50)
    private String sido;

    @Column(name = "sigungu", length = 50)
    private String sigungu;

    @Column(name = "operator_name", length = 100)
    private String operatorName;

    @Column(name = "operator_call", length = 20)
    private String operatorCall;

    @Column(name = "parking_free", length = 20)
    private String parkingFree;

    @Column(name = "note", columnDefinition = "TEXT")
    private String note;
}