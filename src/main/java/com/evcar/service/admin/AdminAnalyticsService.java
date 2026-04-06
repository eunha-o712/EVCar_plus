package com.evcar.service.admin;

import com.evcar.dto.admin.AdminConsultationResultResponseDto;
import com.evcar.dto.admin.AdminMonthlyConsultationResponseDto;
import com.evcar.dto.admin.AdminRegionConsultationResponseDto;
import com.evcar.dto.admin.AdminStatsSummaryResponseDto;
import com.evcar.dto.admin.AdminVehicleDemandResponseDto;
import java.util.List;

public interface AdminAnalyticsService {

    AdminStatsSummaryResponseDto getSummary();

    List<AdminMonthlyConsultationResponseDto> getMonthlyConsultationStats();

    List<AdminVehicleDemandResponseDto> getVehicleDemandStats();

    List<AdminRegionConsultationResponseDto> getRegionConsultationStats();

    List<AdminConsultationResultResponseDto> getConsultationResultStats();
}