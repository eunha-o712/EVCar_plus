package com.evcar.service.chatbot;

import com.evcar.dto.admin.AdminDashboardResponseDto;
import com.evcar.dto.chatbot.AdminDashboardAiRequestDto;
import com.evcar.dto.chatbot.AdminDashboardAiResponseDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FastApiDashboardAnalysisServiceImpl implements FastApiDashboardAnalysisService {

    private final WebClient fastApiWebClient;

    @Override
    public String analyzeDashboard(AdminDashboardResponseDto dashboardResponseDto) {
        AdminDashboardAiRequestDto requestDto = toRequestDto(dashboardResponseDto);

        try {
            AdminDashboardAiResponseDto responseDto = fastApiWebClient.post()
                    .uri("/ai/admin/dashboard/analyze")
                    .bodyValue(requestDto)
                    .retrieve()
                    .bodyToMono(AdminDashboardAiResponseDto.class)
                    .block();

            if (responseDto == null || responseDto.getSummary() == null || responseDto.getSummary().isBlank()) {
                return createFallbackSummary(dashboardResponseDto);
            }

            return responseDto.getSummary();
        } catch (Exception exception) {
            return createFallbackSummary(dashboardResponseDto);
        }
    }

    private AdminDashboardAiRequestDto toRequestDto(AdminDashboardResponseDto dashboardResponseDto) {
        List<AdminDashboardAiRequestDto.MonthlyStatDto> monthlyStats = dashboardResponseDto.getMonthlyConsultationStats()
                .stream()
                .map(stat -> AdminDashboardAiRequestDto.MonthlyStatDto.builder()
                        .monthLabel(stat.getMonthLabel())
                        .consultationCount(stat.getConsultationCount())
                        .build())
                .toList();

        List<AdminDashboardAiRequestDto.TopVehicleStatDto> topVehicles = dashboardResponseDto.getTopVehicleStats()
                .stream()
                .map(stat -> AdminDashboardAiRequestDto.TopVehicleStatDto.builder()
                        .modelName(stat.getModelName())
                        .consultationCount(stat.getConsultationCount())
                        .percentage(stat.getPercentage())
                        .build())
                .toList();

        List<AdminDashboardAiRequestDto.RegionStatDto> regionStats = dashboardResponseDto.getRegionConsultationStats()
                .stream()
                .map(stat -> AdminDashboardAiRequestDto.RegionStatDto.builder()
                        .regionName(stat.getRegionName())
                        .consultationCount(stat.getConsultationCount())
                        .percentage(stat.getPercentage())
                        .build())
                .toList();

        return AdminDashboardAiRequestDto.builder()
                .todayReservationCount(dashboardResponseDto.getTodayReservationCount())
                .inProgressCount(dashboardResponseDto.getInProgressCount())
                .completedCount(dashboardResponseDto.getCompletedCount())
                .monthlyStats(monthlyStats)
                .topVehicles(topVehicles)
                .regionStats(regionStats)
                .build();
    }

    private String createFallbackSummary(AdminDashboardResponseDto dashboardResponseDto) {
        StringBuilder summaryBuilder = new StringBuilder();

        summaryBuilder.append("[AI-OFFLINE] ");

        summaryBuilder.append("금일 예약은 ")
                .append(dashboardResponseDto.getTodayReservationCount())
                .append("건이며, ");

        if (!dashboardResponseDto.getMonthlyConsultationStats().isEmpty()) {
            AdminDashboardResponseDto.MonthlyConsultationStatDto latestMonthStat =
                    dashboardResponseDto.getMonthlyConsultationStats()
                            .get(dashboardResponseDto.getMonthlyConsultationStats().size() - 1);

            summaryBuilder.append(latestMonthStat.getMonthLabel())
                    .append(" 상담 등록 건수는 ")
                    .append(latestMonthStat.getConsultationCount())
                    .append("건입니다. ");
        }

        if (!dashboardResponseDto.getTopVehicleStats().isEmpty()) {
            AdminDashboardResponseDto.TopVehicleStatDto topVehicleStat = dashboardResponseDto.getTopVehicleStats().get(0);
            summaryBuilder.append(topVehicleStat.getModelName())
                    .append(" 차량이 가장 높은 관심을 받고 있습니다. ");
        }

        if (!dashboardResponseDto.getRegionConsultationStats().isEmpty()) {
            AdminDashboardResponseDto.RegionConsultationStatDto topRegionStat =
                    dashboardResponseDto.getRegionConsultationStats().get(0);

            summaryBuilder.append(topRegionStat.getRegionName())
                    .append(" 지역 상담 비중이 가장 높습니다.");
        }

        return summaryBuilder.toString();
    }
}