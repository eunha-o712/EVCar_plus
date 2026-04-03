<<<<<<< HEAD
package com.evcar.repository.admin;

import com.evcar.dto.admin.AdminUserDetailResponseDto;
import com.evcar.dto.admin.AdminUserListResponseDto;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminUserQueryRepository {

    Page<AdminUserListResponseDto> findUserPage(String status, String keyword, Pageable pageable);

    Optional<AdminUserDetailResponseDto> findUserDetail(String userId);
=======
package com.evcar.repository.admin;

import com.evcar.dto.admin.AdminUserDetailResponseDto;
import com.evcar.dto.admin.AdminUserListResponseDto;

import java.util.List;

public interface AdminUserQueryRepository {

    List<AdminUserListResponseDto> findUserList(String status, String keyword);

    AdminUserDetailResponseDto findUserDetail(String userId);
>>>>>>> ccef92c2bb418a9ae53ae1d629a62927994a692e
}