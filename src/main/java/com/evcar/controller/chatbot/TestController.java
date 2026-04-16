package com.evcar.controller.chatbot;

import com.evcar.service.chatbot.FastApiHealthCheckService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final FastApiHealthCheckService healthCheckService;

    @GetMapping("/test/fastapi")
    public String testFastApi() {
        boolean isAlive = healthCheckService.checkHealth();

        if (isAlive) {
            return "FastAPI 연결 성공";
        } else {
            return "FastAPI 연결 실패";
        }
    }
}