package com.evcar.service.chatbot;

import com.evcar.dto.admin.AdminDashboardResponseDto;
import com.evcar.dto.chatbot.AdminDashboardAiRequestDto;
import com.evcar.dto.chatbot.AdminDashboardAiResponseDto;
import java.util.Collections;
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
    public AdminDashboardAiResponseDto analyzeDashboard(AdminDashboardResponseDto dashboardResponseDto) {
        AdminDashboardAiRequestDto requestDto = toRequestDto(dashboardResponseDto);

        try {
            AdminDashboardAiResponseDto responseDto = fastApiWebClient.post()
                    .uri("/ai/admin/dashboard/analyze")
                    .bodyValue(requestDto)
                    .retrieve()
                    .bodyToMono(AdminDashboardAiResponseDto.class)
                    .block();

            if (responseDto == null || isBlank(responseDto.getSummary())) {
                return createFallbackResponse(dashboardResponseDto);
            }

            return AdminDashboardAiResponseDto.builder()
                    .summary(defaultText(responseDto.getSummary(), "AI 요약 결과가 없습니다."))
                    .trend(defaultText(responseDto.getTrend(), "상담 추이 데이터가 부족합니다."))
                    .vehicle(defaultText(responseDto.getVehicle(), "차량 관심도 데이터가 부족합니다."))
                    .region(defaultText(responseDto.getRegion(), "지역 분포 데이터가 부족합니다."))
                    .build();

        } catch (Exception exception) {
            return createFallbackResponse(dashboardResponseDto);
        }
    }

    private AdminDashboardAiRequestDto toRequestDto(AdminDashboardResponseDto dashboardResponseDto) {
        List<AdminDashboardResponseDto.MonthlyConsultationStatDto> monthlySource =
                dashboardResponseDto.getMonthlyConsultationStats() == null
                        ? Collections.emptyList()
                        : dashboardResponseDto.getMonthlyConsultationStats();

        List<AdminDashboardResponseDto.TopVehicleStatDto> topVehicleSource =
                dashboardResponseDto.getTopVehicleStats() == null
                        ? Collections.emptyList()
                        : dashboardResponseDto.getTopVehicleStats();

        List<AdminDashboardResponseDto.RegionConsultationStatDto> regionSource =
                dashboardResponseDto.getRegionConsultationStats() == null
                        ? Collections.emptyList()
                        : dashboardResponseDto.getRegionConsultationStats();

        List<AdminDashboardAiRequestDto.MonthlyStatDto> monthlyStats = monthlySource.stream()
                .map(stat -> AdminDashboardAiRequestDto.MonthlyStatDto.builder()
                        .monthLabel(stat.getMonthLabel())
                        .consultationCount(stat.getConsultationCount())
                        .build())
                .toList();

        List<AdminDashboardAiRequestDto.TopVehicleStatDto> topVehicles = topVehicleSource.stream()
                .map(stat -> AdminDashboardAiRequestDto.TopVehicleStatDto.builder()
                        .modelName(stat.getModelName())
                        .consultationCount(stat.getConsultationCount())
                        .percentage(stat.getPercentage())
                        .build())
                .toList();

        List<AdminDashboardAiRequestDto.RegionStatDto> regionStats = regionSource.stream()
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

    private AdminDashboardAiResponseDto createFallbackResponse(AdminDashboardResponseDto dashboardResponseDto) {
        String trend = createTrendFallback(dashboardResponseDto);
        String vehicle = createVehicleFallback(dashboardResponseDto);
        String region = createRegionFallback(dashboardResponseDto);
        String reservation = createReservationFallback(dashboardResponseDto);

        String summary = "[AI-OFFLINE] " + trend + " " + vehicle + " " + region + " " + reservation;

        return AdminDashboardAiResponseDto.builder()
                .summary(summary)
                .trend(trend)
                .vehicle(vehicle)
                .region(region)
                .build();
    }

    private String createTrendFallback(AdminDashboardResponseDto dashboardResponseDto) {
        List<AdminDashboardResponseDto.MonthlyConsultationStatDto> stats =
                dashboardResponseDto.getMonthlyConsultationStats() == null
                        ? Collections.emptyList()
                        : dashboardResponseDto.getMonthlyConsultationStats();

        if (stats.isEmpty()) {
            return "최근 월간 상담 데이터가 부족하여 추세 분석이 어렵습니다.";
        }

        if (stats.size() == 1) {
            AdminDashboardResponseDto.MonthlyConsultationStatDto current = stats.get(0);
            return current.getMonthLabel() + " 상담 등록 건수는 " + current.getConsultationCount() + "건입니다.";
        }

        AdminDashboardResponseDto.MonthlyConsultationStatDto previous = stats.get(stats.size() - 2);
        AdminDashboardResponseDto.MonthlyConsultationStatDto current = stats.get(stats.size() - 1);

        if (current.getConsultationCount() > previous.getConsultationCount()) {
            return "최근 상담 등록 건수는 " + previous.getMonthLabel() + " 대비 "
                    + current.getMonthLabel() + "에 증가 추세를 보이고 있습니다.";
        } else if (current.getConsultationCount() < previous.getConsultationCount()) {
            return "최근 상담 등록 건수는 " + previous.getMonthLabel() + " 대비 "
                    + current.getMonthLabel() + "에 감소 추세를 보이고 있습니다.";
        }

        return "최근 상담 등록 건수는 " + current.getMonthLabel()
                + " 기준으로 전월과 유사한 수준을 유지하고 있습니다.";
    }

    private String createVehicleFallback(AdminDashboardResponseDto dashboardResponseDto) {
        List<AdminDashboardResponseDto.TopVehicleStatDto> stats =
                dashboardResponseDto.getTopVehicleStats() == null
                        ? Collections.emptyList()
                        : dashboardResponseDto.getTopVehicleStats();

        if (stats.isEmpty()) {
            return "차량별 상담 데이터가 부족하여 인기 차량 분석이 어렵습니다.";
        }

        AdminDashboardResponseDto.TopVehicleStatDto topVehicle = stats.get(0);
        return "인기 차량은 " + topVehicle.getModelName() + "이며 상담 "
                + topVehicle.getConsultationCount() + "건, 점유율 "
                + topVehicle.getPercentage() + "%를 기록하고 있습니다.";
    }

    private String createRegionFallback(AdminDashboardResponseDto dashboardResponseDto) {
        List<AdminDashboardResponseDto.RegionConsultationStatDto> stats =
                dashboardResponseDto.getRegionConsultationStats() == null
                        ? Collections.emptyList()
                        : dashboardResponseDto.getRegionConsultationStats();

        if (stats.isEmpty()) {
            return "지역별 상담 데이터가 부족하여 지역 분석이 어렵습니다.";
        }

        AdminDashboardResponseDto.RegionConsultationStatDto topRegion = stats.get(0);
        return "지역별로는 " + topRegion.getRegionName() + " 상담 비중이 가장 높으며 "
                + topRegion.getConsultationCount() + "건, "
                + topRegion.getPercentage() + "% 수준입니다.";
    }

    private String createReservationFallback(AdminDashboardResponseDto dashboardResponseDto) {
        if (dashboardResponseDto.getTodayReservationCount() > 0) {
            return "금일 예약은 " + dashboardResponseDto.getTodayReservationCount() + "건으로 집계되었습니다.";
        }
        return "금일 예약 건수는 아직 집계되지 않았습니다.";
    }

    private String defaultText(String value, String fallback) {
        return isBlank(value) ? fallback : value;
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}