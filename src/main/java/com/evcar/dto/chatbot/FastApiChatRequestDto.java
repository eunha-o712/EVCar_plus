package com.evcar.dto.chatbot;

import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class FastApiChatRequestDto {

    private String userMessage;
    private List<VehicleChatbotDto> vehicles;
    private List<ConsultationChatbotDto> consultations;
}