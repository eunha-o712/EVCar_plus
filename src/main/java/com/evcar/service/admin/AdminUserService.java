package com.evcar.service.admin;

import com.evcar.dto.admin.AdminUserDetailResponseDto;
import com.evcar.dto.admin.AdminUserListResponseDto;

import java.util.List;

public interface AdminUserService {

    List<AdminUserListResponseDto> getUserList(String status, String keyword);

    AdminUserDetailResponseDto getUserDetail(String userId);
}