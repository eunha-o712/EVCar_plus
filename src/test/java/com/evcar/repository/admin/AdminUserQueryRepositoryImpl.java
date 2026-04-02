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
}