package com.evcar.repository.admin;

import com.evcar.dto.admin.AdminUserDetailResponseDto;
import com.evcar.dto.admin.AdminUserListResponseDto;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminUserQueryRepository {

    Page<AdminUserListResponseDto> findUserPage(String status, String keyword, Pageable pageable);

    Optional<AdminUserDetailResponseDto> findUserDetail(String userId);
}