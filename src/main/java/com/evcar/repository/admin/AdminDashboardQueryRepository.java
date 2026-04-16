package com.evcar.repository.admin;

import com.evcar.dto.admin.AdminDashboardResponseDto.MonthlyConsultationStatDto;
import com.evcar.dto.admin.AdminDashboardResponseDto.RegionConsultationStatDto;
import com.evcar.dto.admin.AdminDashboardResponseDto.TopVehicleStatDto;
import java.util.List;

public interface AdminDashboardQueryRepository {

    long countTodayReservation();

    long countByConsultStatus(String consultStatus);

    List<MonthlyConsultationStatDto> findMonthlyConsultationStats(int monthCount);

    List<TopVehicleStatDto> findTopVehicleStats(int limit);

    List<RegionConsultationStatDto> findRegionConsultationStats();
}