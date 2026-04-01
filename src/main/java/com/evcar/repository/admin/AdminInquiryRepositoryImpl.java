package com.evcar.repository.admin;

import com.evcar.dto.admin.AdminInquiryDetailDto;
import com.evcar.dto.admin.AdminInquiryListItemDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
@RequiredArgsConstructor
public class AdminInquiryRepositoryImpl implements AdminInquiryQueryRepository {

    private static final String STATUS_WAITING = "WAITING";
    private static final String STATUS_COMPLETED = "COMPLETED";

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public List<AdminInquiryListItemDto> findInquiryPage(int offset, int size, String replyStatus, String keyword) {
        StringBuilder sql = new StringBuilder("""
                SELECT
                    i.inquiry_id,
                    i.user_id,
                    u.name,
                    i.title,
                    i.reply_status,
                    i.created_at,
                    CASE
                        WHEN i.reply_status IN ('REPLIED', 'CLOSED') THEN 1
                        WHEN i.reply_content IS NULL OR TRIM(i.reply_content) = '' THEN 0
                        ELSE 1
                    END AS has_reply
                FROM inquiry i
                INNER JOIN user u ON u.user_id = i.user_id
                WHERE 1 = 1
                """);

        appendReplyStatusCondition(sql, replyStatus);
        appendKeywordCondition(sql, keyword);

        sql.append("""
                ORDER BY i.created_at DESC, i.inquiry_id DESC
                LIMIT :size OFFSET :offset
                """);

        Query query = entityManager.createNativeQuery(sql.toString());
        bindConditions(query, replyStatus, keyword);
        query.setParameter("size", size);
        query.setParameter("offset", offset);

        @SuppressWarnings("unchecked")
        List<Object[]> rows = query.getResultList();

        return rows.stream()
                .map(this::toListItemDto)
                .toList();
    }

    @Override
    public long countInquiries(String replyStatus, String keyword) {
        StringBuilder sql = new StringBuilder("""
                SELECT COUNT(*)
                FROM inquiry i
                INNER JOIN user u ON u.user_id = i.user_id
                WHERE 1 = 1
                """);

        appendReplyStatusCondition(sql, replyStatus);
        appendKeywordCondition(sql, keyword);

        Query query = entityManager.createNativeQuery(sql.toString());
        bindConditions(query, replyStatus, keyword);

        Number count = (Number) query.getSingleResult();
        return count.longValue();
    }

    @Override
    public long countAllInquiries(String keyword) {
        return countInquiries(null, keyword);
    }

    @Override
    public long countWaitingInquiries(String keyword) {
        return countInquiries(STATUS_WAITING, keyword);
    }

    @Override
    public long countCompletedInquiries(String keyword) {
        return countInquiries(STATUS_COMPLETED, keyword);
    }

    @Override
    public Optional<AdminInquiryDetailDto> findInquiryDetail(String inquiryId) {
        String sql = """
                SELECT
                    i.inquiry_id,
                    i.user_id,
                    u.name,
                    u.phone,
                    u.email,
                    i.title,
                    i.content,
                    i.reply_content,
                    i.reply_status,
                    i.created_at,
                    i.updated_at
                FROM inquiry i
                INNER JOIN user u ON u.user_id = i.user_id
                WHERE i.inquiry_id = :inquiryId
                """;

        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("inquiryId", inquiryId);

        @SuppressWarnings("unchecked")
        List<Object[]> rows = query.getResultList();

        if (rows.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(toDetailDto(rows.get(0)));
    }

    @Override
    public void updateReply(String inquiryId, String replyContent, String replyStatus) {
        String sql = """
                UPDATE inquiry
                SET reply_content = :replyContent,
                    reply_status = :replyStatus,
                    updated_at = CURRENT_DATE
                WHERE inquiry_id = :inquiryId
                """;

        entityManager.createNativeQuery(sql)
                .setParameter("replyContent", replyContent)
                .setParameter("replyStatus", replyStatus)
                .setParameter("inquiryId", inquiryId)
                .executeUpdate();
    }

    private void appendReplyStatusCondition(StringBuilder sql, String replyStatus) {
        if (!StringUtils.hasText(replyStatus)) {
            return;
        }

        if (STATUS_WAITING.equals(replyStatus)) {
            sql.append(" AND i.reply_status = 'WAITING' ");
            return;
        }

        if (STATUS_COMPLETED.equals(replyStatus)) {
            sql.append(" AND i.reply_status IN ('REPLIED', 'CLOSED') ");
        }
    }

    private void appendKeywordCondition(StringBuilder sql, String keyword) {
        if (!StringUtils.hasText(keyword)) {
            return;
        }

        sql.append("""
                 AND (
                    i.title LIKE CONCAT('%', :keyword, '%')
                    OR i.content LIKE CONCAT('%', :keyword, '%')
                    OR u.name LIKE CONCAT('%', :keyword, '%')
                    OR i.user_id LIKE CONCAT('%', :keyword, '%')
                )
                """);
    }

    private void bindConditions(Query query, String replyStatus, String keyword) {
        if (StringUtils.hasText(keyword)) {
            query.setParameter("keyword", keyword.trim());
        }
    }

    private AdminInquiryListItemDto toListItemDto(Object[] row) {
        return AdminInquiryListItemDto.builder()
                .inquiryId((String) row[0])
                .userId((String) row[1])
                .userName((String) row[2])
                .title((String) row[3])
                .replyStatus((String) row[4])
                .createdAt(toLocalDate(row[5]))
                .hasReply(((Number) row[6]).intValue() == 1)
                .build();
    }

    private AdminInquiryDetailDto toDetailDto(Object[] row) {
        return AdminInquiryDetailDto.builder()
                .inquiryId((String) row[0])
                .userId((String) row[1])
                .userName((String) row[2])
                .phone((String) row[3])
                .email((String) row[4])
                .title((String) row[5])
                .content((String) row[6])
                .replyContent((String) row[7])
                .replyStatus((String) row[8])
                .createdAt(toLocalDate(row[9]))
                .updatedAt(toLocalDate(row[10]))
                .build();
    }

    private LocalDate toLocalDate(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Date date) {
            return date.toLocalDate();
        }
        if (value instanceof LocalDate localDate) {
            return localDate;
        }
        return LocalDate.parse(String.valueOf(value));
    }
}