package com.evcar.service.admin;

import com.evcar.dto.admin.AdminFaqFormResponseDto;
import com.evcar.dto.admin.AdminFaqPageResponseDto;
import com.evcar.dto.admin.AdminFaqSaveRequestDto;

public interface AdminFaqService {

    AdminFaqPageResponseDto getFaqPage(int page);

    AdminFaqFormResponseDto getFaq(String faqId);

    void saveFaq(AdminFaqSaveRequestDto requestDto);

    void deleteFaq(String faqId);
}