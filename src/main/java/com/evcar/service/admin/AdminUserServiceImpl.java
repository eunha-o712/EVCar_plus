<<<<<<< HEAD
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
=======
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
>>>>>>> ccef92c2bb418a9ae53ae1d629a62927994a692e
}