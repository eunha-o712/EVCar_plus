package com.evcar.service.admin;

import com.evcar.dto.admin.AdminAnalyticsComparisonResponseDto;
import com.evcar.dto.admin.AdminConsultationResultResponseDto;
import com.evcar.dto.admin.AdminMonthlyConsultationResponseDto;
import com.evcar.dto.admin.AdminRegionConsultationResponseDto;
import com.evcar.dto.admin.AdminStatsSummaryResponseDto;
import com.evcar.dto.admin.AdminVehicleDemandResponseDto;
import com.evcar.repository.consultation.ConsultationRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
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
        LocalDateTime latestCreatedAt = consultationRepository.findLatestCreatedAt();

        YearMonth baseYearMonth = latestCreatedAt == null
                ? YearMonth.now()
                : YearMonth.from(latestCreatedAt);

        YearMonth startYearMonth = baseYearMonth.minusMonths(11L);

        LocalDateTime startDateTime = startYearMonth.atDay(1).atStartOfDay();
        LocalDateTime endDateTime = baseYearMonth.atEndOfMonth().atTime(LocalTime.MAX);

        List<ConsultationRepository.MonthlyConsultationProjection> rawStats =
                consultationRepository.findMonthlyConsultationStats(startDateTime, endDateTime);

        Map<String, Long> monthlyCountMap = new HashMap<>();
        for (ConsultationRepository.MonthlyConsultationProjection item : rawStats) {
            monthlyCountMap.put(
                    item.getMonthLabel(),
                    item.getConsultationCount() == null ? 0L : item.getConsultationCount()
            );
        }

        List<AdminMonthlyConsultationResponseDto> result = new ArrayList<>();

        YearMonth current = startYearMonth;

        while (!current.isAfter(baseYearMonth)) {

            String monthLabel = current.toString();
            long consultationCount = monthlyCountMap.getOrDefault(monthLabel, 0L);

            result.add(
                AdminMonthlyConsultationResponseDto.builder()
                    .monthLabel(monthLabel)
                    .consultationCount(consultationCount)
                    .build()
            );

            current = current.plusMonths(1);
        }

        return result;
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

    @Override
    public AdminAnalyticsComparisonResponseDto getComparison() {
        LocalDate now = LocalDate.now();

        LocalDateTime currentStartDateTime = now.minusMonths(11L).withDayOfMonth(1).atStartOfDay();
        LocalDateTime currentEndDateTime = now.withDayOfMonth(now.lengthOfMonth()).atTime(LocalTime.MAX);

        LocalDateTime previousStartDateTime = currentStartDateTime.minusYears(1L);
        LocalDateTime previousEndDateTime = currentEndDateTime.minusYears(1L);

        long currentConsultationCount = consultationRepository.countByCreatedAtBetween(currentStartDateTime, currentEndDateTime);
        long previousConsultationCount = consultationRepository.countByCreatedAtBetween(previousStartDateTime, previousEndDateTime);

        long currentPurchaseCount = consultationRepository.countByConsultResultAndCreatedAtBetween(
                CONSULT_RESULT_PURCHASE, currentStartDateTime, currentEndDateTime
        );
        long previousPurchaseCount = consultationRepository.countByConsultResultAndCreatedAtBetween(
                CONSULT_RESULT_PURCHASE, previousStartDateTime, previousEndDateTime
        );

        int currentConversionRate = currentConsultationCount == 0
                ? 0
                : (int) Math.round((double) currentPurchaseCount * 100 / currentConsultationCount);

        int previousConversionRate = previousConsultationCount == 0
                ? 0
                : (int) Math.round((double) previousPurchaseCount * 100 / previousConsultationCount);

        int consultationChangeRate = previousConsultationCount == 0
                ? 0
                : (int) Math.round(((double) (currentConsultationCount - previousConsultationCount) / previousConsultationCount) * 100);

        int conversionRateGap = currentConversionRate - previousConversionRate;

        ConsultationRepository.TopVehicleProjection topVehicleProjection =
                consultationRepository.findTopVehicleDemandStats(currentStartDateTime, currentEndDateTime);

        ConsultationRepository.TopRegionProjection topRegionProjection =
                consultationRepository.findTopRegionConsultationStats(currentStartDateTime, currentEndDateTime);

        return AdminAnalyticsComparisonResponseDto.builder()
                .currentConsultationCount(currentConsultationCount)
                .previousConsultationCount(previousConsultationCount)
                .consultationChangeRate(consultationChangeRate)
                .currentConversionRate(currentConversionRate)
                .previousConversionRate(previousConversionRate)
                .conversionRateGap(conversionRateGap)
                .topModelName(topVehicleProjection == null ? "-" : topVehicleProjection.getModelName())
                .topRegionName(topRegionProjection == null ? "-" : topRegionProjection.getRegionName())
                .build();
    }
}