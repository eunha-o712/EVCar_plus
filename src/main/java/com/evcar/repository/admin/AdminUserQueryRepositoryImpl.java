<<<<<<< HEAD
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
                    CONCAT(u.address, CASE
                        WHEN u.address_detail IS NULL OR TRIM(u.address_detail) = '' THEN ''
                        ELSE CONCAT(' ', u.address_detail)
                    END) AS full_address,
                    u.email,
                    u.user_status,
                    u.role,
                    u.vehicle_model,
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
                .vehicleModel((String) row[10])
                .vehicleYear((String) row[11])
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

        String text = value.toString();
        return LocalDate.parse(text.length() >= 10 ? text.substring(0, 10) : text);
    }
=======
package com.evcar.repository.admin;

import com.evcar.dto.admin.AdminUserDetailResponseDto;
import com.evcar.dto.admin.AdminUserListResponseDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class AdminUserQueryRepositoryImpl implements AdminUserQueryRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<AdminUserListResponseDto> findUserList(String status, String keyword) {
        StringBuilder sql = new StringBuilder("""
                SELECT
                    u.user_id,
                    u.login_id,
                    u.name,
                    u.phone,
                    u.email,
                    u.user_status,
                    u.role,
                    u.has_vehicle,
                    u.vehicle_year,
                    u.driving_distance,
                    u.created_at,
                    u.withdrawn_at
                FROM user u
                WHERE 1 = 1
                  AND u.role = :role
                """);

        if (StringUtils.hasText(status) && !"ALL".equalsIgnoreCase(status)) {
            sql.append(" AND u.user_status = :status ");
        }

        if (StringUtils.hasText(keyword)) {
            sql.append("""
                     AND (
                        u.name LIKE :keyword
                        OR u.phone LIKE :keyword
                        OR u.login_id LIKE :keyword
                        OR u.email LIKE :keyword
                     )
                    """);
        }

        sql.append(" ORDER BY u.created_at DESC, u.user_id DESC ");

        Query query = entityManager.createNativeQuery(sql.toString());
        query.setParameter("role", "USER");

        if (StringUtils.hasText(status) && !"ALL".equalsIgnoreCase(status)) {
            query.setParameter("status", status);
        }

        if (StringUtils.hasText(keyword)) {
            query.setParameter("keyword", "%" + keyword.trim() + "%");
        }

        @SuppressWarnings("unchecked")
        List<Object[]> results = query.getResultList();

        List<AdminUserListResponseDto> userList = new ArrayList<>();
        for (Object[] row : results) {
            userList.add(AdminUserListResponseDto.builder()
                    .userId(toStringValue(row[0]))
                    .loginId(toStringValue(row[1]))
                    .name(toStringValue(row[2]))
                    .phone(toStringValue(row[3]))
                    .email(toStringValue(row[4]))
                    .userStatus(toStringValue(row[5]))
                    .role(toStringValue(row[6]))
                    .hasVehicle(toBoolean(row[7]))
                    .vehicleYear(toInteger(row[8]))
                    .drivingDistance(toInteger(row[9]))
                    .createdAt(toLocalDateTime(row[10]))
                    .withdrawnAt(toLocalDateTime(row[11]))
                    .build());
        }

        return userList;
    }

    @Override
    public AdminUserDetailResponseDto findUserDetail(String userId) {
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
                    u.created_at,
                    u.withdrawn_at,
                    u.updated_at
                FROM user u
                WHERE u.user_id = :userId
                  AND u.role = :role
                """;

        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("userId", userId);
        query.setParameter("role", "USER");

        @SuppressWarnings("unchecked")
        List<Object[]> results = query.getResultList();

        if (results.isEmpty()) {
            return null;
        }

        Object[] row = results.get(0);

        return AdminUserDetailResponseDto.builder()
                .userId(toStringValue(row[0]))
                .loginId(toStringValue(row[1]))
                .name(toStringValue(row[2]))
                .birthDate(toLocalDate(row[3]))
                .gender(toStringValue(row[4]))
                .phone(toStringValue(row[5]))
                .address(toStringValue(row[6]))
                .email(toStringValue(row[7]))
                .userStatus(toStringValue(row[8]))
                .role(toStringValue(row[9]))
                .hasVehicle(toBoolean(row[10]))
                .vehicleYear(toInteger(row[11]))
                .drivingDistance(toInteger(row[12]))
                .createdAt(toLocalDateTime(row[13]))
                .withdrawnAt(toLocalDateTime(row[14]))
                .updatedAt(toLocalDateTime(row[15]))
                .build();
    }

    private Long toLong(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Number number) {
            return number.longValue();
        }
        return Long.parseLong(value.toString());
    }

    private Integer toInteger(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Number number) {
            return number.intValue();
        }
        return Integer.parseInt(value.toString());
    }

    private Boolean toBoolean(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Boolean booleanValue) {
            return booleanValue;
        }
        if (value instanceof Number number) {
            return number.intValue() == 1;
        }
        return Boolean.parseBoolean(value.toString());
    }

    private String toStringValue(Object value) {
        return value == null ? null : value.toString();
    }

    private LocalDate toLocalDate(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof LocalDate localDate) {
            return localDate;
        }
        if (value instanceof java.sql.Date sqlDate) {
            return sqlDate.toLocalDate();
        }
        if (value instanceof Timestamp timestamp) {
            return timestamp.toLocalDateTime().toLocalDate();
        }
        return LocalDate.parse(value.toString());
    }

    private LocalDateTime toLocalDateTime(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof LocalDateTime localDateTime) {
            return localDateTime;
        }
        if (value instanceof Timestamp timestamp) {
            return timestamp.toLocalDateTime();
        }
        if (value instanceof java.sql.Date sqlDate) {
            return sqlDate.toLocalDate().atStartOfDay();
        }

        String text = value.toString().trim();

        if (text.length() == 10) {
            return LocalDate.parse(text).atStartOfDay();
        }

        return LocalDateTime.parse(text.replace(" ", "T"));
    }
>>>>>>> ccef92c2bb418a9ae53ae1d629a62927994a692e
}