package com.evcar.domain.charging;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "charging_station")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChargingStation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "station_id")
    private Long stationId;

    @Column(name = "station_name", nullable = false, length = 100)
    private String stationName;

    @Column(nullable = false)
    private double lat;

    @Column(nullable = false)
    private double lng;

    @Column(nullable = false, length = 200)
    private String address;

    @Column(name = "charger_type", length = 20)
    private String chargerType;

    @Column(length = 20)
    private String status;
}