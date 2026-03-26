package com.evcar.service.user;

import com.evcar.dto.user.UserSignupDto;

public interface UserService {

    // 회원가입
    void signup(UserSignupDto dto);

    // 아이디 중복 체크
    boolean isUserLoginIdDuplicate(String userLoginId);
}