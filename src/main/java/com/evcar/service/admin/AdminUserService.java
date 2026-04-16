package com.evcar.service.admin;

import com.evcar.dto.admin.AdminUserDetailResponseDto;
import com.evcar.dto.admin.AdminUserListResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminUserService {

    Page<AdminUserListResponseDto> getUserPage(String status, String keyword, Pageable pageable);

    AdminUserDetailResponseDto getUserDetail(String userId);

}