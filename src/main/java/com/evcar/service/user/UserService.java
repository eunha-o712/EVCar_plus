<<<<<<< HEAD
package com.evcar.service.user;

import com.evcar.dto.user.UserSignupDto;

public interface UserService {

    // 회원가입
    void signup(UserSignupDto dto);

    // 아이디 중복 체크
    boolean isUserLoginIdDuplicate(String loginId);

    // 이메일 중복 체크
    boolean isUserEmailDuplicate(String email);
}
=======
package com.evcar.service.user;

import com.evcar.dto.user.UserSignupDto;

public interface UserService {

    // 회원가입
    void signup(UserSignupDto dto);

    // 아이디 중복 체크
    boolean isUserLoginIdDuplicate(String userLoginId);

    // 이메일 중복 체크 🔥 추가
    boolean isUserEmailDuplicate(String email);
}
>>>>>>> ccef92c2bb418a9ae53ae1d629a62927994a692e
