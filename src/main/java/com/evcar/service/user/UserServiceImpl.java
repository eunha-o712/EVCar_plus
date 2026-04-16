package com.evcar.service.user;

import com.evcar.domain.user.User;
import com.evcar.dto.user.UserSignupDto;
import com.evcar.repository.user.UserRepository;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private String generateUserId() {
        return userRepository.findLastUserId()
                .map(last -> {
                    int num = Integer.parseInt(last.replace("user", "")) + 1;
                    return String.format("user%04d", num);
                })
                .orElse("user0001");
    }

    @Override
    @Transactional
    public void signup(UserSignupDto dto) {
        if (!dto.isAgreeTerms() || !dto.isAgreePrivacy()) {
            throw new IllegalArgumentException("필수 약관 동의가 필요합니다.");
        }

        if (userRepository.existsByLoginId(dto.getLoginId())) {
            throw new IllegalArgumentException("이미 사용 중인 아이디입니다.");
        }

        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        if (!dto.getPassword().equals(dto.getPasswordConfirm())) {
            throw new IllegalArgumentException("비밀번호 확인이 일치하지 않습니다.");
        }

        String vehicleModel = dto.getVehicleModel() == null ? "" : dto.getVehicleModel().trim();
        String vehicleYear = dto.getVehicleYear() == null ? "" : dto.getVehicleYear().trim();
        Integer drivingDistance = dto.getDrivingDistance() == null ? 0 : dto.getDrivingDistance();

        User user = User.builder()
                .userId(generateUserId())
                .loginId(dto.getLoginId())
                .password(passwordEncoder.encode(dto.getPassword()))
                .name(dto.getName())
                .birthDate(
                        dto.getBirthDate() != null && !dto.getBirthDate().isEmpty()
                                ? LocalDate.parse(dto.getBirthDate())
                                : null
                )
                .gender(dto.getGender())
                .phone(dto.getPhone())
                .address(dto.getAddress())
                .addressDetail(dto.getAddressDetail() == null ? "" : dto.getAddressDetail().trim())
                .email(dto.getEmail())
                .vehicleModel(vehicleModel)
                .vehicleYear(vehicleYear)
                .drivingDistance(drivingDistance)
                .build();

        userRepository.save(user);
    }

    @Override
    public boolean isUserLoginIdDuplicate(String loginId) {
        return userRepository.existsByLoginId(loginId);
    }

    @Override
    public boolean isUserEmailDuplicate(String email) {
        return userRepository.existsByEmail(email);
    }
}