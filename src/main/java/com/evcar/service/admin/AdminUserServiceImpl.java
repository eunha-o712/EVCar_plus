package com.evcar.service.admin;

import com.evcar.dto.admin.AdminUserDetailResponseDto;
import com.evcar.dto.admin.AdminUserListResponseDto;
import com.evcar.repository.admin.AdminUserQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminUserServiceImpl implements AdminUserService {

    private final AdminUserQueryRepository adminUserQueryRepository;

    @Override
    public Page<AdminUserListResponseDto> getUserPage(String status, String keyword, Pageable pageable) {
        return adminUserQueryRepository.findUserPage(status, keyword, pageable);
    }

    @Override
    public AdminUserDetailResponseDto getUserDetail(String userId) {
        return adminUserQueryRepository.findUserDetail(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원 정보를 찾을 수 없습니다."));
    }

}