package com.evcar.controller.admin;

import com.evcar.dto.admin.AdminAnalyticsComparisonResponseDto;
import com.evcar.dto.admin.AdminConsultationResultResponseDto;
import com.evcar.dto.admin.AdminMonthlyConsultationResponseDto;
import com.evcar.dto.admin.AdminRegionConsultationResponseDto;
import com.evcar.dto.admin.AdminStatsSummaryResponseDto;
import com.evcar.dto.admin.AdminVehicleDemandResponseDto;
import com.evcar.service.admin.AdminAnalyticsService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/analytics")
public class AdminAnalyticsController {

    private final AdminAnalyticsService adminAnalyticsService;

    @GetMapping("")
    public String analyticsDashboard() {
        return "admin/analytics/dashboard";
    }

    @GetMapping("/api/summary")
    @ResponseBody
    public AdminStatsSummaryResponseDto getSummary() {
        return adminAnalyticsService.getSummary();
    }

    @GetMapping("/api/monthly")
    @ResponseBody
    public List<AdminMonthlyConsultationResponseDto> getMonthlyStats() {
        return adminAnalyticsService.getMonthlyConsultationStats();
    }

    @GetMapping("/api/vehicle-demand")
    @ResponseBody
    public List<AdminVehicleDemandResponseDto> getVehicleDemandStats() {
        return adminAnalyticsService.getVehicleDemandStats();
    }

    @GetMapping("/api/region")
    @ResponseBody
    public List<AdminRegionConsultationResponseDto> getRegionStats() {
        return adminAnalyticsService.getRegionConsultationStats();
    }

    @GetMapping("/api/result")
    @ResponseBody
    public List<AdminConsultationResultResponseDto> getResultStats() {
        return adminAnalyticsService.getConsultationResultStats();
    }

    @GetMapping("/api/comparison")
    @ResponseBody
    public AdminAnalyticsComparisonResponseDto getComparison() {
        return adminAnalyticsService.getComparison();
    }
}