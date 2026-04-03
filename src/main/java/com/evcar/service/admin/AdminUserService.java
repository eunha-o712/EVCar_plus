<<<<<<< HEAD
package com.evcar.service.admin;

import com.evcar.dto.admin.AdminUserDetailResponseDto;
import com.evcar.dto.admin.AdminUserListResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminUserService {

    Page<AdminUserListResponseDto> getUserPage(String status, String keyword, Pageable pageable);

    AdminUserDetailResponseDto getUserDetail(String userId);
=======
package com.evcar.service.admin;

import com.evcar.dto.admin.AdminUserDetailResponseDto;
import com.evcar.dto.admin.AdminUserListResponseDto;

import java.util.List;

public interface AdminUserService {

    List<AdminUserListResponseDto> getUserList(String status, String keyword);

    AdminUserDetailResponseDto getUserDetail(String userId);
>>>>>>> ccef92c2bb418a9ae53ae1d629a62927994a692e
}