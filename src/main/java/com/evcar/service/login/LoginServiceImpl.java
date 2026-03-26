package com.evcar.service.login;

import com.evcar.domain.user.User;
import com.evcar.dto.login.IdRecoveryDto;
import com.evcar.dto.login.LoginRequestDto;
import com.evcar.dto.login.PasswordResetDto;
import com.evcar.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LoginServiceImpl implements LoginService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void verifyUser(PasswordResetDto dto) {
        userRepository
                .findByLoginIdAndNameAndEmail(
                        dto.getLoginId(),
                        dto.getName(),
                        dto.getEmail()
                )
                .orElseThrow(() -> new IllegalArgumentException("일치하는 회원이 없습니다."));
    }

    @Override
    public User login(LoginRequestDto dto) {

        User user = userRepository.findByLoginId(dto.getLoginId())
                .orElseThrow(() -> new IllegalArgumentException("아이디 또는 비밀번호가 올바르지 않습니다."));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("아이디 또는 비밀번호가 올바르지 않습니다.");
        }

        return user;
    }

    @Override
    public String findUserLoginId(IdRecoveryDto dto) {

        User user = userRepository
                .findByNameAndEmail(dto.getName(), dto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("입력하신 정보와 일치하는 회원이 없습니다."));

        return user.getLoginId();
    }

    @Override
    @Transactional
    public void resetPassword(PasswordResetDto dto) {

        User user = userRepository
                .findByLoginIdAndNameAndEmail(
                        dto.getLoginId(),
                        dto.getName(),
                        dto.getEmail()
                )
                .orElseThrow(() -> new IllegalArgumentException("아이디, 이름, 이메일이 일치하지 않습니다."));

        if (!dto.getNewPassword().equals(dto.getNewPasswordConfirm())) {
            throw new IllegalArgumentException("비밀번호 확인이 일치하지 않습니다.");
        }

        user.changePassword(passwordEncoder.encode(dto.getNewPassword()));
    }
}