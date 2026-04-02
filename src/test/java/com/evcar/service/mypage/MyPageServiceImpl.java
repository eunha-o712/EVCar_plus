package com.evcar.service.mypage;

import com.evcar.domain.consultation.Consultation;
import com.evcar.domain.user.User;
import com.evcar.domain.user.UserStatus;
import com.evcar.dto.mypage.MyConsultationResponseDto;
import com.evcar.dto.mypage.MyInquiryResponseDto;
import com.evcar.dto.mypage.MyPageInfoResponseDto;
import com.evcar.dto.mypage.MyPageInfoUpdateRequestDto;
import com.evcar.dto.mypage.WithdrawRequestDto;
import com.evcar.repository.consultation.ConsultationRepository;
import com.evcar.repository.inquiry.InquiryRepository;
import com.evcar.repository.user.UserRepository;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MyPageServiceImpl implements MyPageService {

    private static final String CONSULT_STATUS_IN_PROGRESS = "진행중";

    private final UserRepository userRepository;
    private final ConsultationRepository consultationRepository;
    private final InquiryRepository inquiryRepository;

    @Override
    public MyPageInfoResponseDto getMyPageInfo(String loginId) {
        User user = getUserByLoginId(loginId);
        return MyPageInfoResponseDto.from(user);
    }

    @Override
    @Transactional
    public void updateMyPageInfo(String loginId, MyPageInfoUpdateRequestDto requestDto) {
        User user = getUserByLoginId(loginId);

        String name = requestDto.getName() != null ? requestDto.getName() : user.getName();
        LocalDate birthDate = requestDto.getBirthDate() != null ? requestDto.getBirthDate() : user.getBirthDate();
        String gender = requestDto.getGender() != null ? requestDto.getGender() : user.getGender();
        String phone = requestDto.getPhone() != null ? requestDto.getPhone() : user.getPhone();
        String address = requestDto.getAddress() != null ? requestDto.getAddress() : user.getAddress();
        String addressDetail = requestDto.getAddressDetail() != null ? requestDto.getAddressDetail() : user.getAddressDetail();
        String email = requestDto.getEmail() != null ? requestDto.getEmail() : user.getEmail();

        String hasVehicle = requestDto.getHasVehicle() != null ? requestDto.getHasVehicle() : user.getHasVehicle();
        String vehicleModel = requestDto.getVehicleModel();
        String vehicleYear = requestDto.getVehicleYear();
        Integer drivingDistance = requestDto.getDrivingDistance();

        if (requestDto.isNovehicle()) {
            vehicleModel = null;
            vehicleYear = null;
            drivingDistance = null;
        }

        if (requestDto.hasInvalidPasswordChangeInput()) {
            throw new IllegalArgumentException("비밀번호 변경 항목을 모두 입력해주세요.");
        }

        if (requestDto.isNewPasswordMismatch()) {
            throw new IllegalArgumentException("새 비밀번호와 새 비밀번호 확인이 일치하지 않습니다.");
        }

        if (requestDto.hasPasswordChangeRequest()) {
            if (!user.getPassword().equals(requestDto.getCurrentPassword())) {
                throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
            }

            user.changePassword(requestDto.getNewPassword());
        }

        user.updateMyPageInfo(
                name,
                birthDate,
                gender,
                phone,
                address,
                addressDetail,
                email,
                hasVehicle,
                vehicleModel,
                vehicleYear,
                drivingDistance
        );
    }

    @Override
    public List<MyConsultationResponseDto> getMyConsultations(String loginId) {
        User user = getUserByLoginId(loginId);

        return consultationRepository.findByUserUserIdOrderByCreatedAtDesc(user.getUserId()).stream()
                .map(MyConsultationResponseDto::from)
                .toList();
    }

    @Override
    @Transactional
    public void cancelMyConsultation(String loginId, Integer consultId) {
        User user = getUserByLoginId(loginId);

        Consultation consultation = consultationRepository.findById(consultId)
                .orElseThrow(() -> new IllegalArgumentException("상담 정보를 찾을 수 없습니다."));

        if (!consultation.getUser().getUserId().equals(user.getUserId())) {
            throw new IllegalArgumentException("본인의 상담만 취소할 수 있습니다.");
        }

        consultation.cancel();
    }

    @Override
    public List<MyInquiryResponseDto> getMyInquiries(String loginId) {
        User user = getUserByLoginId(loginId);

        return inquiryRepository.findByUserUserIdOrderByCreatedAtDesc(user.getUserId()).stream()
                .map(MyInquiryResponseDto::from)
                .toList();
    }

    @Override
    @Transactional
    public void withdraw(String loginId, WithdrawRequestDto withdrawRequestDto) {
        if (withdrawRequestDto.isInvalid()) {
            throw new IllegalArgumentException("회원탈퇴 입력값을 확인해주세요.");
        }

        User user = getUserByLoginId(loginId);

        if (user.getUserStatus() == UserStatus.WITHDRAWN) {
            throw new IllegalArgumentException("이미 탈퇴 처리된 회원입니다.");
        }

        boolean hasInProgressConsultation =
                consultationRepository.existsByUserUserIdAndConsultStatus(user.getUserId(), CONSULT_STATUS_IN_PROGRESS);

        if (hasInProgressConsultation) {
            throw new IllegalArgumentException("현재 진행중인 상담이 있어 탈퇴가 불가능합니다. 상담 완료 또는 취소 후 다시 시도해주세요.");
        }

        if (!user.getPassword().equals(withdrawRequestDto.getPassword())) {
            throw new IllegalArgumentException("비밀번호를 확인하세요.");
        }

        user.withdraw();
    }

    private User getUserByLoginId(String loginId) {
        return userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new IllegalArgumentException("회원정보를 찾을 수 없습니다."));
    }
}