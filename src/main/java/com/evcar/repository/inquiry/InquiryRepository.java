<<<<<<< HEAD
package com.evcar.repository.inquiry;

import com.evcar.domain.inquiry.Inquiry;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InquiryRepository extends JpaRepository<Inquiry, String> {

    List<Inquiry> findByUserIdOrderByCreatedAtDesc(String userId);
=======
package com.evcar.repository.inquiry;

import com.evcar.domain.inquiry.Inquiry;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InquiryRepository extends JpaRepository<Inquiry, Integer> {

    List<Inquiry> findByUserUserIdOrderByCreatedAtDesc(Integer userId);

    Optional<Inquiry> findByInquiryIdAndUserUserId(Integer inquiryId, Integer userId);
>>>>>>> ccef92c2bb418a9ae53ae1d629a62927994a692e
}