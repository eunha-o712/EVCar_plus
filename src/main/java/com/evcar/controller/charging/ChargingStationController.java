package com.evcar.controller.charging;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ChargingStationController {

    @GetMapping("/map")
    public String mapTest() {
        return "charging/map";
    }
}