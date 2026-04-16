package com.evcar.controller.chatbot;

import com.evcar.dto.chatbot.ChatbotMessageRequestDto;
import com.evcar.dto.chatbot.ChatbotMessageResponseDto;
import com.evcar.service.chatbot.ChatbotService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chatbot")
public class ChatbotController {

    private final ChatbotService chatbotService;

    @PostMapping("/send")
    public ChatbotMessageResponseDto send(@RequestBody ChatbotMessageRequestDto requestDto) {
        String reply = chatbotService.getReply(requestDto.getUserId(), requestDto.getUserMessage());

        return ChatbotMessageResponseDto.builder()
                .reply(reply)
                .build();
    }
}