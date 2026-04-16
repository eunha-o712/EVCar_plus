package com.evcar.service.login;

import com.evcar.domain.user.User;
import com.evcar.dto.login.IdRecoveryDto;
import com.evcar.dto.login.LoginRequestDto;
import com.evcar.dto.login.PasswordResetDto;

public interface LoginService {

    User login(LoginRequestDto dto);

    String findUserLoginId(IdRecoveryDto dto);

    void verifyUser(PasswordResetDto dto);

    void resetPassword(PasswordResetDto dto);
}
