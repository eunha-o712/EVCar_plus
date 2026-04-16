package com.evcar.repository.inquiry;

import com.evcar.domain.inquiry.Inquiry;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InquiryRepository extends JpaRepository<Inquiry, String> {
	
	boolean existsByUserIdAndReplyStatus(String userId, String replyStatus);

    List<Inquiry> findAllByOrderByCreatedAtDesc();

    List<Inquiry> findByTitleContainingIgnoreCaseOrderByCreatedAtDesc(String keyword);

    Optional<Inquiry> findTopByOrderByInquiryIdDesc();

    List<Inquiry> findByUserIdOrderByCreatedAtDesc(String userId);
}