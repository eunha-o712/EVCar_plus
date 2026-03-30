package com.evcar.repository.inquiry;

import com.evcar.domain.inquiry.Inquiry;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InquiryRepository extends JpaRepository<Inquiry, String> {

    List<Inquiry> findByUserUserIdOrderByCreatedAtDesc(String userId);

    Optional<Inquiry> findByInquiryIdAndUserUserId(String inquiryId, String userId);
}