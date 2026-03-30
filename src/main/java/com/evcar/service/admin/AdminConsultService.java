package com.evcar.service.admin;

import com.evcar.dto.admin.AdminConsultDetailResponseDto;
import com.evcar.dto.admin.AdminConsultListResponseDto;
import com.evcar.dto.admin.AdminConsultReplyRequestDto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminConsultService {

    Page<AdminConsultListResponseDto> getConsultPage(String status, String keyword, Pageable pageable);

    AdminConsultDetailResponseDto getConsultDetail(String consultId);

    void replyConsult(String consultId, AdminConsultReplyRequestDto dto);
}