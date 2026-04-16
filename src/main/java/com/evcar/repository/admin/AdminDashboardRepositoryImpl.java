package com.evcar.repository.admin;

import com.evcar.dto.admin.AdminDashboardResponseDto.MonthlyConsultationStatDto;
import com.evcar.dto.admin.AdminDashboardResponseDto.RegionConsultationStatDto;
import com.evcar.dto.admin.AdminDashboardResponseDto.TopVehicleStatDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AdminDashboardRepositoryImpl implements AdminDashboardQueryRepository {

    private static final DateTimeFormatter MONTH_LABEL_FORMATTER = DateTimeFormatter.ofPattern("M월");

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public long countTodayReservation() {
        String sql = """
                SELECT COUNT(*)
                FROM consultation
                WHERE DATE(preferred_datetime) = CURRENT_DATE
                """;

        Number result = (Number) entityManager.createNativeQuery(sql).getSingleResult();
        return result.longValue();
    }

    @Override
    public long countByConsultStatus(String consultStatus) {
        String sql = """
                SELECT COUNT(*)
                FROM consultation
                WHERE consult_status = :consultStatus
                """;

        Number result = (Number) entityManager.createNativeQuery(sql)
                .setParameter("consultStatus", consultStatus)
                .getSingleResult();

        return result.longValue();
    }

    @Override
    public List<MonthlyConsultationStatDto> findMonthlyConsultationStats(int monthCount) {
        String sql = """
                SELECT month_key, consultation_count
                FROM (
                    SELECT DATE_FORMAT(created_at, '%Y-%m') AS month_key,
                           COUNT(*) AS consultation_count
                    FROM consultation
                    GROUP BY DATE_FORMAT(created_at, '%Y-%m')
                    ORDER BY month_key DESC
                    LIMIT :monthCount
                ) t
                ORDER BY month_key ASC
                """;

        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("monthCount", monthCount);

        @SuppressWarnings("unchecked")
        List<Object[]> rows = query.getResultList();

        return rows.stream()
                .map(this::toMonthlyDto)
                .toList();
    }

    private MonthlyConsultationStatDto toMonthlyDto(Object[] row) {
        return MonthlyConsultationStatDto.builder()
                .monthLabel((String) row[0]) // "2025-03"
                .consultationCount(((Number) row[1]).longValue())
                .build();
    }

    @Override
    public List<TopVehicleStatDto> findTopVehicleStats(int limit) {
        String sql = """
                SELECT
                    v.model_name,
                    COUNT(*) AS consultation_count,
                    ROUND(COUNT(*) * 100.0 / total_stats.total_count, 1) AS percentage
                FROM consultation c
                INNER JOIN vehicle v ON v.vehicle_id = c.vehicle_id
                CROSS JOIN (
                    SELECT COUNT(*) AS total_count
                    FROM consultation
                ) total_stats
                GROUP BY v.model_name, total_stats.total_count
                ORDER BY consultation_count DESC, v.model_name ASC
                LIMIT :limit
                """;

        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("limit", limit);

        @SuppressWarnings("unchecked")
        List<Object[]> rows = query.getResultList();

        return rows.stream()
                .map(this::toTopVehicleStatDto)
                .toList();
    }

    @Override
    public List<RegionConsultationStatDto> findRegionConsultationStats() {
        String sql = """
                SELECT
                    region_stats.region_name,
                    region_stats.consultation_count,
                    ROUND(region_stats.consultation_count * 100.0 / total_stats.total_count, 1) AS percentage
                FROM (
                    SELECT
                        CASE
                            WHEN u.address IS NULL OR TRIM(u.address) = '' THEN '기타'
                            ELSE SUBSTRING_INDEX(TRIM(u.address), ' ', 1)
                        END AS region_name,
                        COUNT(*) AS consultation_count
                    FROM consultation c
                    INNER JOIN user u ON u.user_id = c.user_id
                    GROUP BY
                        CASE
                            WHEN u.address IS NULL OR TRIM(u.address) = '' THEN '기타'
                            ELSE SUBSTRING_INDEX(TRIM(u.address), ' ', 1)
                        END
                ) region_stats
                CROSS JOIN (
                    SELECT COUNT(*) AS total_count
                    FROM consultation
                ) total_stats
                ORDER BY region_stats.consultation_count DESC, region_stats.region_name ASC
                """;

        @SuppressWarnings("unchecked")
        List<Object[]> rows = entityManager.createNativeQuery(sql).getResultList();

        return rows.stream()
                .map(this::toRegionConsultationStatDto)
                .sorted(Comparator.comparingLong(RegionConsultationStatDto::getConsultationCount).reversed()
                        .thenComparing(RegionConsultationStatDto::getRegionName))
                .toList();
    }

    private MonthlyConsultationStatDto toMonthlyConsultationStatDto(Object[] row) {
        LocalDate monthStart = toLocalDate(row[0]);

        return MonthlyConsultationStatDto.builder()
                .monthLabel(monthStart.format(MONTH_LABEL_FORMATTER))
                .consultationCount(((Number) row[1]).longValue())
                .build();
    }

    private TopVehicleStatDto toTopVehicleStatDto(Object[] row) {
        return TopVehicleStatDto.builder()
                .modelName((String) row[0])
                .consultationCount(((Number) row[1]).longValue())
                .percentage(toDouble(row[2]))
                .build();
    }

    private RegionConsultationStatDto toRegionConsultationStatDto(Object[] row) {
        return RegionConsultationStatDto.builder()
                .regionName((String) row[0])
                .consultationCount(((Number) row[1]).longValue())
                .percentage(toDouble(row[2]))
                .build();
    }

    private LocalDate toLocalDate(Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof Date date) {
            return date.toLocalDate();
        }

        return LocalDate.parse(String.valueOf(value));
    }

    private double toDouble(Object value) {
        if (value == null) {
            return 0.0;
        }

        if (value instanceof BigDecimal bigDecimal) {
            return bigDecimal.setScale(1, RoundingMode.HALF_UP).doubleValue();
        }

        if (value instanceof Number number) {
            return BigDecimal.valueOf(number.doubleValue())
                    .setScale(1, RoundingMode.HALF_UP)
                    .doubleValue();
        }

        return BigDecimal.valueOf(Double.parseDouble(String.valueOf(value)))
                .setScale(1, RoundingMode.HALF_UP)
                .doubleValue();
    }
}