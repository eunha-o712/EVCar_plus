package com.evcar.repository.admin;

import com.evcar.dto.admin.AdminUserDetailResponseDto;
import com.evcar.dto.admin.AdminUserListResponseDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
public class AdminUserQueryRepositoryImpl implements AdminUserQueryRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<AdminUserListResponseDto> findUserPage(String status, String keyword, Pageable pageable) {
        StringBuilder whereClause = new StringBuilder(" WHERE 1 = 1 ");

        if (StringUtils.hasText(status)) {
            whereClause.append(" AND u.user_status = :status ");
        }

        if (StringUtils.hasText(keyword)) {
            whereClause.append("""
                    AND (
                        u.name LIKE :nameKeyword
                        OR RIGHT(u.phone, 4) LIKE :phoneKeyword
                    )
                    """);
        }

        String dataSql = """
                SELECT
                    u.user_id,
                    u.login_id,
                    u.name,
                    u.phone,
                    u.email,
                    u.user_status,
                    CAST(u.created_at AS CHAR)
                FROM user u
                """
                + whereClause
                + " ORDER BY u.created_at DESC, u.user_id DESC LIMIT :limit OFFSET :offset ";

        String countSql = """
                SELECT COUNT(*)
                FROM user u
                """
                + whereClause;

        Query dataQuery = entityManager.createNativeQuery(dataSql);
        Query countQuery = entityManager.createNativeQuery(countSql);

        if (StringUtils.hasText(status)) {
            dataQuery.setParameter("status", status);
            countQuery.setParameter("status", status);
        }

        if (StringUtils.hasText(keyword)) {
            dataQuery.setParameter("nameKeyword", "%" + keyword + "%");
            countQuery.setParameter("nameKeyword", "%" + keyword + "%");
            dataQuery.setParameter("phoneKeyword", "%" + keyword + "%");
            countQuery.setParameter("phoneKeyword", "%" + keyword + "%");
        }

        dataQuery.setParameter("limit", pageable.getPageSize());
        dataQuery.setParameter("offset", pageable.getOffset());

        @SuppressWarnings("unchecked")
        List<Object[]> rows = dataQuery.getResultList();

        List<AdminUserListResponseDto> content = new ArrayList<>();
        for (Object[] row : rows) {
            content.add(AdminUserListResponseDto.builder()
                    .userId((String) row[0])
                    .loginId((String) row[1])
                    .name((String) row[2])
                    .phone((String) row[3])
                    .email((String) row[4])
                    .userStatus((String) row[5])
                    .createdAt(row[6] == null ? "" : row[6].toString())
                    .build());
        }

        Number totalCount = (Number) countQuery.getSingleResult();
        return new PageImpl<>(content, pageable, totalCount.longValue());
    }

    @Override
    public Optional<AdminUserDetailResponseDto> findUserDetail(String userId) {
        String sql = """
                SELECT
                    u.user_id,
                    u.login_id,
                    u.name,
                    u.birth_date,
                    u.gender,
                    u.phone,
                    u.address,
                    u.email,
                    u.user_status,
                    u.role,
                    u.has_vehicle,
                    u.vehicle_year,
                    u.driving_distance,
                    CAST(u.created_at AS CHAR),
                    CAST(u.updated_at AS CHAR)
                FROM user u
                WHERE u.user_id = :userId
                """;

        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("userId", userId);

        @SuppressWarnings("unchecked")
        List<Object[]> rows = query.getResultList();

        if (rows.isEmpty()) {
            return Optional.empty();
        }

        Object[] row = rows.get(0);

        return Optional.of(AdminUserDetailResponseDto.builder()
                .userId((String) row[0])
                .loginId((String) row[1])
                .name((String) row[2])
                .birthDate(toLocalDate(row[3]))
                .gender((String) row[4])
                .phone((String) row[5])
                .address((String) row[6])
                .email((String) row[7])
                .userStatus((String) row[8])
                .role((String) row[9])
                .hasVehicle((String) row[10])
                .vehicleYear(row[11] == null ? null : ((Number) row[11]).intValue())
                .drivingDistance(row[12] == null ? null : ((Number) row[12]).intValue())
                .createdAt(row[13] == null ? null : row[13].toString())
                .updatedAt(row[14] == null ? null : row[14].toString())
                .build());
    }

    private LocalDate toLocalDate(Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof java.sql.Date date) {
            return date.toLocalDate();
        }

        if (value instanceof LocalDate localDate) {
            return localDate;
        }

        return LocalDate.parse(value.toString());
    }
}