package com.evcar.service.chatbot;

public interface FastApiChatbotService {

    String getReply(String userId, String userMessage);
}