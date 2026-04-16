package com.evcar.controller.charging;

import com.evcar.dto.charging.ChargerResponseDto;
import com.evcar.dto.charging.ChargingRegionResponseDto;
import com.evcar.dto.charging.ChargingStationResponseDto;
import com.evcar.repository.charging.ChargerRepository;
import com.evcar.service.charging.ChargingStationApiService;
import com.evcar.service.charging.ChargingStationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/charging")
@RequiredArgsConstructor
public class ChargingStationController {

    private final ChargingStationApiService chargingStationApiService;
    private final ChargingStationService chargingStationService;
    private final ChargerRepository chargerRepository;

    @Value("${ev.map.kakao.javascript-key}")
    private String kakaoJavascriptKey;

    @GetMapping("/map")
    public String map(Model model) {
        model.addAttribute("kakaoJavascriptKey", kakaoJavascriptKey);
        return "charging/map";
    }

    @GetMapping("/load")
    @ResponseBody
    public String load() {
        chargingStationApiService.loadData();
        return "적재 완료";
    }

    @GetMapping("/status/refresh")
    @ResponseBody
    public String refreshStatus() {
        chargingStationApiService.refreshChargerStatus();
        return "충전기 상태 동기화 완료";
    }

    @GetMapping("/regions")
    @ResponseBody
    public List<ChargingRegionResponseDto> getRegions() {
        return chargingStationService.getAllRegions();
    }

    @GetMapping("/stations")
    @ResponseBody
    public List<ChargingStationResponseDto> getStations(
            @RequestParam("zcode") String zcode,
            @RequestParam(value = "zscode", required = false, defaultValue = "") String zscode
    ) {
        return chargingStationService.getStations(zcode, zscode);
    }

    @GetMapping("/chargers")
    @ResponseBody
    public List<ChargerResponseDto> getChargers(@RequestParam("stationId") String stationId) {
        return chargerRepository.findByChargingStation_StationIdOrderByChargerIdAsc(stationId)
                .stream()
                .map(ChargerResponseDto::from)
                .toList();
    }
}