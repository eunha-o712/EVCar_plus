package com.evcar.repository.faq;

import com.evcar.domain.faq.Faq;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FaqRepository extends JpaRepository<Faq, String> {

    List<Faq> findAllByOrderByCreatedAtDesc();

    List<Faq> findByQuestionContainingIgnoreCaseOrAnswerContainingIgnoreCaseOrderByCreatedAtDesc(
            String questionKeyword,
            String answerKeyword
    );

    @Query(
            value = """
                    SELECT *
                    FROM faq
                    ORDER BY created_at DESC, faq_id DESC
                    LIMIT :limit OFFSET :offset
                    """,
            nativeQuery = true
    )
    List<Faq> findFaqPage(
            @Param("limit") int limit,
            @Param("offset") int offset
    );

    Optional<Faq> findByFaqId(String faqId);

    @Query(
            value = """
                    SELECT faq_id
                    FROM faq
                    ORDER BY CAST(SUBSTRING(faq_id, 4) AS UNSIGNED) DESC
                    LIMIT 1
                    """,
            nativeQuery = true
    )
    Optional<String> findLatestFaqId();
}