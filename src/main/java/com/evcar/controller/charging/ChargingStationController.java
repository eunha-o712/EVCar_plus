package com.evcar.controller.charging;

import com.evcar.dto.charging.ChargingStationDTO;
import com.evcar.service.charging.ChargingStationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/charging")
public class ChargingStationController {

    private final ChargingStationService chargingStationService;

    // 지도 페이지
    @GetMapping("/map")
    public String mapPage() {
        return "charging/map";
    }

    // 충전소 데이터 조회 API
    @GetMapping("/current")
    @ResponseBody
    public List<ChargingStationDTO> getStations() {
        return chargingStationService.getStations();
    }

    // 🔥 공공데이터 → DB 저장 실행 API
    @GetMapping("/init")
    @ResponseBody
    public String init() {
        chargingStationService.saveFromApi();
        return "DB 저장 완료";
    }
}