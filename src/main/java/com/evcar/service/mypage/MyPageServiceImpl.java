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
    public MyPageInfoResponseDto getMyPageInfo(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("회원정보를 찾을 수 없습니다."));

        return MyPageInfoResponseDto.from(user);
    }

    @Override
    @Transactional
    public void updateMyPageInfo(Integer userId, MyPageInfoUpdateRequestDto requestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("회원정보를 찾을 수 없습니다."));

        String hasVehicle = requestDto.getHasVehicle();
        String vehicleModel = requestDto.getVehicleModel();
        String vehicleYear = requestDto.getVehicleYear();
        Integer drivingDistance = requestDto.getDrivingDistance();

        if (requestDto.isNovehicle()) {
            vehicleModel = null;
            vehicleYear = null;
            drivingDistance = null;
        }

        user.updateMyPageInfo(
                requestDto.getName(),
                requestDto.getBirthDate(),
                requestDto.getGender(),
                requestDto.getPhone(),
                requestDto.getAddress(),
                requestDto.getAddressDetail(),
                requestDto.getEmail(),
                hasVehicle,
                vehicleModel,
                vehicleYear,
                drivingDistance
        );
    }

    @Override
    public List<MyConsultationResponseDto> getMyConsultations(Integer userId) {
        return consultationRepository.findByUserUserIdOrderByCreatedAtDesc(userId).stream()
                .map(MyConsultationResponseDto::from)
                .toList();
    }

    @Override
    @Transactional
    public void cancelMyConsultation(Integer userId, Integer consultId) {
        Consultation consultation = consultationRepository.findById(consultId)
                .orElseThrow(() -> new IllegalArgumentException("상담 정보를 찾을 수 없습니다."));

        if (!consultation.getUser().getUserId().equals(userId)) {
            throw new IllegalArgumentException("본인의 상담만 취소할 수 있습니다.");
        }

        consultation.cancel();
    }

    @Override
    public List<MyInquiryResponseDto> getMyInquiries(Integer userId) {
        return inquiryRepository.findByUserUserIdOrderByCreatedAtDesc(userId).stream()
                .map(MyInquiryResponseDto::from)
                .toList();
    }

    @Override
    @Transactional
    public void withdraw(Integer userId, WithdrawRequestDto requestDto) {
        if (requestDto.isInvalid()) {
            throw new IllegalArgumentException("회원탈퇴 입력값을 확인해주세요.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("회원정보를 찾을 수 없습니다."));

        if (user.getUserStatus() == UserStatus.WITHDRAWN) {
            throw new IllegalArgumentException("이미 탈퇴 처리된 회원입니다.");
        }

        boolean hasInProgressConsultation =
                consultationRepository.existsByUserUserIdAndConsultStatus(userId, CONSULT_STATUS_IN_PROGRESS);

        if (hasInProgressConsultation) {
            throw new IllegalArgumentException("현재 진행중인 상담이 있어 탈퇴가 불가능합니다. 상담 완료 또는 취소 후 다시 시도해주세요.");
        }

        if (!user.getPassword().equals(requestDto.getPassword())) {
            throw new IllegalArgumentException("비밀번호를 확인하세요.");
        }

        user.withdraw();
    }
}