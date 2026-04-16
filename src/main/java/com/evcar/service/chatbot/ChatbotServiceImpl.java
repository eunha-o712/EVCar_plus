package com.evcar.service.chatbot;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatbotServiceImpl implements ChatbotService {

    private final FastApiChatbotService fastApiChatbotService;

    @Override
    public String getReply(String userId, String userMessage) {
        return fastApiChatbotService.getReply(userId, userMessage);
    }
}