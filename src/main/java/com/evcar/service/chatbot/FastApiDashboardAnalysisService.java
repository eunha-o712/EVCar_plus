package com.evcar.service.chatbot;

import com.evcar.dto.admin.AdminDashboardResponseDto;
import com.evcar.dto.chatbot.AdminDashboardAiResponseDto;

public interface FastApiDashboardAnalysisService {

    AdminDashboardAiResponseDto analyzeDashboard(AdminDashboardResponseDto dashboardResponseDto);
}