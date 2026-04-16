package com.evcar.repository.admin;

import com.evcar.dto.admin.AdminConsultDetailResponseDto;
import com.evcar.dto.admin.AdminConsultListResponseDto;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminConsultQueryRepository {

    Page<AdminConsultListResponseDto> findConsultPage(String status, String keyword, Pageable pageable);

    Optional<AdminConsultDetailResponseDto> findConsultDetail(String consultId);
}