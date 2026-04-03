<<<<<<< HEAD
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
=======
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
>>>>>>> ccef92c2bb418a9ae53ae1d629a62927994a692e
