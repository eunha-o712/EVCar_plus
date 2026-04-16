package com.evcar.domain.charging;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "charger")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Charger {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "charger_pk", nullable = false)
    private Long chargerPk;

    @Column(name = "charger_id", length = 50, nullable = false)
    private String chargerId;

    @Column(name = "charger_type", length = 30, nullable = false)
    private String chargerType;

    @Column(name = "power_type", length = 30)
    private String powerType;

    @Column(name = "status", length = 30)
    private String status;

    @Column(name = "status_updated_at")
    private LocalDateTime statusUpdatedAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "station_id", referencedColumnName = "station_id", nullable = false)
    private ChargingStation chargingStation;
}