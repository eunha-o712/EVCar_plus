package com.evcar.dto.chatbot;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminDashboardAiResponseDto {

    private String summary;
    private String trend;
    private String vehicle;
    private String region;
}