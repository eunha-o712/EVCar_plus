package com.evcar.service.charging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChargingSyncScheduler {

    private final ChargingStationApiService chargingStationApiService;

    public void syncStationsHourly() {
        try {
            log.info("충전소 기본정보 동기화 시작");
            chargingStationApiService.loadData();
            log.info("충전소 기본정보 동기화 종료");
        } catch (Exception e) {
            log.error("충전소 기본정보 동기화 실패", e);
        }
    }

    public void refreshStatusEveryFiveMinutes() {
        try {
            log.info("충전기 상태 동기화 시작");
            chargingStationApiService.refreshChargerStatus();
            log.info("충전기 상태 동기화 종료");
        } catch (Exception e) {
            log.error("충전기 상태 동기화 실패", e);
        }
    }
}