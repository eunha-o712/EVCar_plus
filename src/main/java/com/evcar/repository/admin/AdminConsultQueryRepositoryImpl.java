package com.evcar.repository.admin;

import com.evcar.dto.admin.AdminConsultDetailResponseDto;
import com.evcar.dto.admin.AdminConsultListResponseDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
public class AdminConsultQueryRepositoryImpl implements AdminConsultQueryRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<AdminConsultListResponseDto> findConsultPage(String status, String keyword, Pageable pageable) {
        StringBuilder whereClause = new StringBuilder(" WHERE 1 = 1 ");

        if (StringUtils.hasText(status)) {
            whereClause.append(" AND c.consult_status = :status ");
        }

        if (StringUtils.hasText(keyword)) {
            whereClause.append("""
                    AND (
                        u.name LIKE :keyword
                        OR v.model_name LIKE :keyword
                    )
                    """);
        }

        String dataSql = """
                SELECT
                    c.consult_id,
                    u.name,
                    v.model_name,
                    c.preferred_datetime,
                    c.consult_status
                FROM consultation c
                INNER JOIN user u ON c.user_id = u.user_id
                INNER JOIN vehicle v ON c.vehicle_id = v.vehicle_id
                """
                + whereClause
                + " ORDER BY c.created_at DESC, c.consult_id DESC LIMIT :limit OFFSET :offset ";

        String countSql = """
                SELECT COUNT(*)
                FROM consultation c
                INNER JOIN user u ON c.user_id = u.user_id
                INNER JOIN vehicle v ON c.vehicle_id = v.vehicle_id
                """
                + whereClause;

        Query dataQuery = entityManager.createNativeQuery(dataSql);
        Query countQuery = entityManager.createNativeQuery(countSql);

        if (StringUtils.hasText(status)) {
            dataQuery.setParameter("status", status);
            countQuery.setParameter("status", status);
        }

        if (StringUtils.hasText(keyword)) {
            dataQuery.setParameter("keyword", "%" + keyword + "%");
            countQuery.setParameter("keyword", "%" + keyword + "%");
        }

        dataQuery.setParameter("limit", pageable.getPageSize());
        dataQuery.setParameter("offset", pageable.getOffset());

        @SuppressWarnings("unchecked")
        List<Object[]> rows = dataQuery.getResultList();

        List<AdminConsultListResponseDto> content = new ArrayList<>();
        for (Object[] row : rows) {
            content.add(AdminConsultListResponseDto.builder()
                    .consultId((String) row[0])
                    .userName((String) row[1])
                    .vehicleModelName((String) row[2])
                    .consultationDateTime((String) row[3])
                    .consultStatus((String) row[4])
                    .build());
        }

        Number totalCount = (Number) countQuery.getSingleResult();
        return new PageImpl<>(content, pageable, totalCount.longValue());
    }

    @Override
    public Optional<AdminConsultDetailResponseDto> findConsultDetail(String consultId) {
        String sql = """
                SELECT
                    c.consult_id,
                    c.user_id,
                    u.name,
                    c.vehicle_id,
                    v.model_name,
                    c.preferred_datetime,
                    c.budget,
                    c.purchase_plan,
                    c.consult_content,
                    c.consult_status,
                    c.consult_result,
                    c.admin_reply,
                    CAST(c.created_at AS CHAR),
                    CAST(c.updated_at AS CHAR)
                FROM consultation c
                INNER JOIN user u ON c.user_id = u.user_id
                INNER JOIN vehicle v ON c.vehicle_id = v.vehicle_id
                WHERE c.consult_id = :consultId
                """;

        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("consultId", consultId);

        @SuppressWarnings("unchecked")
        List<Object[]> rows = query.getResultList();

        if (rows.isEmpty()) {
            return Optional.empty();
        }

        Object[] row = rows.get(0);

        return Optional.of(AdminConsultDetailResponseDto.builder()
                .consultId((String) row[0])
                .userId((String) row[1])
                .userName((String) row[2])
                .vehicleId((String) row[3])
                .vehicleModelName((String) row[4])
                .preferredDatetime((String) row[5])
                .budget(row[6] == null ? null : ((Number) row[6]).intValue())
                .purchasePlan((String) row[7])
                .consultationContent((String) row[8])
                .consultStatus((String) row[9])
                .consultResult((String) row[10])
                .adminReply((String) row[11])
                .createdAt(row[12] == null ? null : row[12].toString())
                .updatedAt(row[13] == null ? null : row[13].toString())
                .build());
    }
}