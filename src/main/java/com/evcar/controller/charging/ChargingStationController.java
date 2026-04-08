package com.evcar.controller.charging;

import com.evcar.dto.charging.ChargingStationResponseDto;
import com.evcar.service.charging.ChargingStationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/charging")
public class ChargingStationController {

    private final ChargingStationService chargingStationService;

    // 🔥 지도 페이지
    @GetMapping("/map-page")
    public String mapPage() {
        return "charging/map";
    }

    // 🔥 기존 검색 API
    @GetMapping("/region")
    @ResponseBody
    public List<ChargingStationResponseDto> getRegion(
            @RequestParam(name = "sido") String sido,
            @RequestParam(name = "sigungu") String sigungu
    ) {
        return chargingStationService.getStationsByRegion(sido, sigungu);
    }

    // 🔥🔥🔥 추가 (이게 핵심)
    @GetMapping("/regions")
    @ResponseBody
    public Map<String, List<String>> getRegions() {
        return chargingStationService.getAllRegions();
    }
}