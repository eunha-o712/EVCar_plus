package com.evcar.service.admin;

import com.evcar.dto.admin.AdminConsultationResultResponseDto;
import com.evcar.dto.admin.AdminMonthlyConsultationResponseDto;
import com.evcar.dto.admin.AdminRegionConsultationResponseDto;
import com.evcar.dto.admin.AdminStatsSummaryResponseDto;
import com.evcar.dto.admin.AdminVehicleDemandResponseDto;
import com.evcar.repository.consultation.ConsultationRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminAnalyticsServiceImpl implements AdminAnalyticsService {

    private static final String CONSULT_RESULT_PURCHASE = "PURCHASE";

    private final ConsultationRepository consultationRepository;

    @Override
    public AdminStatsSummaryResponseDto getSummary() {
        long totalConsultationCount = consultationRepository.count();

        LocalDate now = LocalDate.now();
        LocalDateTime startOfMonth = now.withDayOfMonth(1).atStartOfDay();
        LocalDateTime endOfMonth = now.withDayOfMonth(now.lengthOfMonth()).atTime(LocalTime.MAX);

        long monthlyConsultationCount = consultationRepository.countByCreatedAtBetween(startOfMonth, endOfMonth);
        long purchaseCount = consultationRepository.countByConsultResult(CONSULT_RESULT_PURCHASE);

        int contractConversionRate = totalConsultationCount == 0
                ? 0
                : (int) Math.round((double) purchaseCount * 100 / totalConsultationCount);

        return AdminStatsSummaryResponseDto.builder()
                .totalConsultationCount(totalConsultationCount)
                .monthlyConsultationCount(monthlyConsultationCount)
                .contractConversionRate(contractConversionRate)
                .build();
    }

    @Override
    public List<AdminMonthlyConsultationResponseDto> getMonthlyConsultationStats() {
        LocalDateTime startDateTime = LocalDate.now()
                .minusMonths(11L)
                .withDayOfMonth(1)
                .atStartOfDay();

        return consultationRepository.findMonthlyConsultationStats(startDateTime)
                .stream()
                .map(item -> AdminMonthlyConsultationResponseDto.builder()
                        .monthLabel(item.getMonthLabel())
                        .consultationCount(item.getConsultationCount() == null ? 0L : item.getConsultationCount())
                        .build())
                .toList();
    }

    @Override
    public List<AdminVehicleDemandResponseDto> getVehicleDemandStats() {
        return consultationRepository.findVehicleDemandStats()
                .stream()
                .map(item -> AdminVehicleDemandResponseDto.builder()
                        .modelName(item.getModelName())
                        .consultationCount(item.getConsultationCount() == null ? 0L : item.getConsultationCount())
                        .build())
                .toList();
    }

    @Override
    public List<AdminRegionConsultationResponseDto> getRegionConsultationStats() {
        return consultationRepository.findRegionConsultationStats()
                .stream()
                .map(item -> AdminRegionConsultationResponseDto.builder()
                        .regionName(item.getRegionName())
                        .consultationCount(item.getConsultationCount() == null ? 0L : item.getConsultationCount())
                        .build())
                .toList();
    }

    @Override
    public List<AdminConsultationResultResponseDto> getConsultationResultStats() {
        return consultationRepository.findConsultationResultStats()
                .stream()
                .map(item -> AdminConsultationResultResponseDto.builder()
                        .resultName(item.getResultName())
                        .consultationCount(item.getConsultationCount() == null ? 0L : item.getConsultationCount())
                        .build())
                .toList();
    }
}