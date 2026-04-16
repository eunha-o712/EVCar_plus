package com.evcar.service.inquiry;

import com.evcar.dto.inquiry.InquiryCreateRequestDto;
import com.evcar.dto.inquiry.InquiryResponseDto;
import java.util.List;

public interface InquiryService {

    List<InquiryResponseDto> getInquiryList(String keyword);

    InquiryResponseDto getInquiryDetail(String inquiryId);

    void createInquiry(InquiryCreateRequestDto requestDto);
}