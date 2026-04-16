package com.evcar.service.chatbot;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
@RequiredArgsConstructor 
public class FastApiHealthCheckService {

    private final WebClient fastApiWebClient;   

    public boolean checkHealth() {
        try {
            Map<String, Object> response = fastApiWebClient.get()
                    .uri("/health")
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            return response != null && "ok".equals(response.get("status"));

        } catch (Exception e) {
            return false;
        }
    }
}