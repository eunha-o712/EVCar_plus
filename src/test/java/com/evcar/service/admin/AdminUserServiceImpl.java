package com.evcar.service.admin;

import com.evcar.dto.admin.AdminUserDetailResponseDto;
import com.evcar.dto.admin.AdminUserListResponseDto;
import com.evcar.repository.admin.AdminUserQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminUserServiceImpl implements AdminUserService {

    private final AdminUserQueryRepository adminUserQueryRepository;

    @Override
    public List<AdminUserListResponseDto> getUserList(String status, String keyword) {
        return adminUserQueryRepository.findUserList(status, keyword);
    }

    @Override
    public AdminUserDetailResponseDto getUserDetail(String userId) {
        return adminUserQueryRepository.findUserDetail(userId);
    }
}