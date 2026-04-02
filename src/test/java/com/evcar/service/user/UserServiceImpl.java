package com.evcar.service.user;

import com.evcar.domain.user.User;
import com.evcar.dto.user.UserSignupDto;
import com.evcar.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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

        User user = User.builder()
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
                .addressDetail(
                	    dto.getAddressDetail() != null && !dto.getAddressDetail().isEmpty()
                	        ? dto.getAddressDetail()
                	        : null
                	)
                .email(dto.getEmail())
                .vehicleModel(
                	    dto.getVehicleModel() != null && !dto.getVehicleModel().isEmpty()
                	        ? dto.getVehicleModel()
                	        : null
                	)
                	.vehicleYear(
                	    dto.getVehicleYear() != null && !dto.getVehicleYear().isEmpty()
                	        ? dto.getVehicleYear()
                	        : null
                	)
                	.drivingDistance(
                	    dto.getDrivingDistance() != null ? dto.getDrivingDistance() : 0
                	)
                .build();

        userRepository.save(user);
    }

    @Override
    public boolean isUserLoginIdDuplicate(String loginId) {

        System.out.println("===== DB 확인 =====");
        System.out.println("loginId = " + loginId);

        boolean result = userRepository.existsByLoginId(loginId);

        System.out.println("중복 여부 = " + result);

        return result;
    }
    @Override
    @Transactional(readOnly = true)
    public boolean isUserEmailDuplicate(String email) {
        return userRepository.existsByEmail(email);
    }
}