package com.evcar.service.admin;

import com.evcar.dto.admin.AdminInquiryDetailDto;
import com.evcar.dto.admin.AdminInquiryPageResponseDto;

public interface AdminInquiryService {

    AdminInquiryPageResponseDto getInquiryPage(int page, int size, String replyStatus, String keyword);

    AdminInquiryDetailDto getInquiryDetail(String inquiryId);

    void saveReply(String inquiryId, String replyContent);
}