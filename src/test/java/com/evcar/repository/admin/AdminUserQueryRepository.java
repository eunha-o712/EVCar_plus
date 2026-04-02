package com.evcar.repository.admin;

import com.evcar.dto.admin.AdminUserDetailResponseDto;
import com.evcar.dto.admin.AdminUserListResponseDto;

import java.util.List;

public interface AdminUserQueryRepository {

    List<AdminUserListResponseDto> findUserList(String status, String keyword);

    AdminUserDetailResponseDto findUserDetail(String userId);
}