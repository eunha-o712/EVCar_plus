package com.evcar.repository.admin;

import com.evcar.dto.admin.AdminInquiryDetailDto;
import com.evcar.dto.admin.AdminInquiryListItemDto;
import java.util.List;
import java.util.Optional;

public interface AdminInquiryQueryRepository {

    List<AdminInquiryListItemDto> findInquiryPage(int offset, int size, String replyStatus, String keyword);

    long countInquiries(String replyStatus, String keyword);

    Optional<AdminInquiryDetailDto> findInquiryDetail(String inquiryId);

    void updateReply(String inquiryId, String replyContent, String replyStatus);
}