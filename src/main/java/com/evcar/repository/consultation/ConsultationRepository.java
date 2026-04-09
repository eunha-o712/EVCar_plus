package com.evcar.repository.consultation;

import com.evcar.domain.consultation.Consultation;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ConsultationRepository extends JpaRepository<Consultation, String> {

    boolean existsByUserIdAndConsultStatus(String userId, String consultStatus);

    List<Consultation> findByUserIdOrderByCreatedAtDesc(String userId);

    long countByCreatedAtBetween(LocalDateTime startDateTime, LocalDateTime endDateTime);

    long countByConsultResult(String consultResult);

    long countByConsultResultAndCreatedAtBetween(String consultResult, LocalDateTime startDateTime, LocalDateTime endDateTime);

    @Query("""
            select max(c.createdAt)
            from Consultation c
            """)
    LocalDateTime findLatestCreatedAt();

    @Query(value = """
            SELECT DATE_FORMAT(c.created_at, '%Y-%m') AS monthLabel,
                   COUNT(*) AS consultationCount
            FROM consultation c
            WHERE c.created_at BETWEEN :startDateTime AND :endDateTime
            GROUP BY DATE_FORMAT(c.created_at, '%Y-%m')
            ORDER BY monthLabel ASC
            """, nativeQuery = true)
    List<MonthlyConsultationProjection> findMonthlyConsultationStats(
            @Param("startDateTime") LocalDateTime startDateTime,
            @Param("endDateTime") LocalDateTime endDateTime
    );

    @Query(value = """
            SELECT v.model_name AS modelName,
                   COUNT(*) AS consultationCount
            FROM consultation c
            INNER JOIN vehicle v
                    ON c.vehicle_id = v.vehicle_id
            GROUP BY v.model_name
            ORDER BY consultationCount DESC, modelName ASC
            """, nativeQuery = true)
    List<VehicleDemandProjection> findVehicleDemandStats();

    @Query(value = """
            SELECT u.address AS regionName,
                   COUNT(*) AS consultationCount
            FROM consultation c
            INNER JOIN user u
                    ON c.user_id = u.user_id
            GROUP BY u.address
            ORDER BY consultationCount DESC, regionName ASC
            """, nativeQuery = true)
    List<RegionConsultationProjection> findRegionConsultationStats();

    @Query(value = """
            SELECT COALESCE(c.consult_result, '미정') AS resultName,
                   COUNT(*) AS consultationCount
            FROM consultation c
            GROUP BY COALESCE(c.consult_result, '미정')
            ORDER BY consultationCount DESC, resultName ASC
            """, nativeQuery = true)
    List<ConsultationResultProjection> findConsultationResultStats();

    @Query(value = """
            SELECT v.model_name AS modelName,
                   COUNT(*) AS consultationCount
            FROM consultation c
            INNER JOIN vehicle v
                    ON c.vehicle_id = v.vehicle_id
            WHERE c.created_at BETWEEN :startDateTime AND :endDateTime
            GROUP BY v.model_name
            ORDER BY consultationCount DESC, modelName ASC
            LIMIT 1
            """, nativeQuery = true)
    TopVehicleProjection findTopVehicleDemandStats(
            @Param("startDateTime") LocalDateTime startDateTime,
            @Param("endDateTime") LocalDateTime endDateTime
    );
    
    @Query(value = """
            SELECT u.address AS regionName,
                   COUNT(*) AS consultationCount
            FROM consultation c
            INNER JOIN user u
                    ON c.user_id = u.user_id
            WHERE c.created_at BETWEEN :startDateTime AND :endDateTime
            GROUP BY u.address
            ORDER BY consultationCount DESC, regionName ASC
            LIMIT 1
            """, nativeQuery = true)
    TopRegionProjection findTopRegionConsultationStats(
            @Param("startDateTime") LocalDateTime startDateTime,
            @Param("endDateTime") LocalDateTime endDateTime
    );

    interface MonthlyConsultationProjection {
        String getMonthLabel();
        Long getConsultationCount();
    }

    interface VehicleDemandProjection {
        String getModelName();
        Long getConsultationCount();
    }

    interface RegionConsultationProjection {
        String getRegionName();
        Long getConsultationCount();
    }

    interface ConsultationResultProjection {
        String getResultName();
        Long getConsultationCount();
    }

    interface TopVehicleProjection {
        String getModelName();
        Long getConsultationCount();
    }

    interface TopRegionProjection {
        String getRegionName();
        Long getConsultationCount();
    }
}
