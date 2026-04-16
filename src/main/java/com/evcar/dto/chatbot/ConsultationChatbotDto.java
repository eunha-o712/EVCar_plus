package com.evcar.dto.chatbot;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ConsultationChatbotDto {

    private String consultId;
    private String consultContent;
    private String consultStatus;
    private String consultResult;
    private String adminReply;
}