package com.evcar.repository.inquiry;

import com.evcar.domain.inquiry.Inquiry;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InquiryRepository extends JpaRepository<Inquiry, String> {

    List<Inquiry> findByUserIdOrderByCreatedAtDesc(String userId);

}