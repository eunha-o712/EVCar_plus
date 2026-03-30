package com.evcar.service.mypage;

import com.evcar.domain.consultation.Consultation;
import com.evcar.domain.inquiry.Inquiry;
import com.evcar.domain.user.User;
import com.evcar.domain.user.UserStatus;
import com.evcar.dto.mypage.MyConsultationResponseDto;
import com.evcar.dto.mypage.MyInquiryResponseDto;
import com.evcar.dto.mypage.MyPageInfoResponseDto;
import com.evcar.dto.mypage.MyPageInfoUpdateRequestDto;
import com.evcar.dto.mypage.MyPageSummaryResponseDto;
import com.evcar.dto.mypage.MyWishlistResponseDto;
import com.evcar.dto.mypage.WithdrawRequestDto;
import com.evcar.repository.consultation.ConsultationRepository;
import com.evcar.repository.inquiry.InquiryRepository;
import com.evcar.repository.user.UserRepository;
import java.time.LocalDate;
import java.time.Year;
import java.time.ZoneId;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MyPageServiceImpl implements MyPageService {

	private static final String CONSULT_STATUS_IN_PROGRESS = "IN_PROGRESS";
    private static final ZoneId KOREA_ZONE_ID = ZoneId.of("Asia/Seoul");
    private static final int MIN_VEHICLE_YEAR = 1900;

    private final UserRepository userRepository;
    private final ConsultationRepository consultationRepository;
    private final InquiryRepository inquiryRepository;

    @Override
    public MyPageInfoResponseDto getMyPageInfo(String userId) {
        User user = getUserByUserId(userId);
        return MyPageInfoResponseDto.from(user);
    }

    @Override
    public MyPageSummaryResponseDto getMyPageSummary(String userId) {
        User user = getUserByUserId(userId);

        List<MyConsultationResponseDto> consultations = getMyConsultations(user.getUserId());
        List<MyInquiryResponseDto> inquiries = getMyInquiries(user.getUserId());
        List<MyWishlistResponseDto> wishlist = getMyWishlist(user.getUserId());

        int waitingInquiryCount = (int) inquiries.stream()
                .filter(inquiry -> !inquiry.isAnswered())
                .count();

        return MyPageSummaryResponseDto.builder()
                .wishlistCount(wishlist.size())
                .consultationCount(consultations.size())
                .inquiryCount(inquiries.size())
                .waitingInquiryCount(waitingInquiryCount)
                .build();
    }

    @Override
    @Transactional
    public void updateMyPageInfo(String userId, MyPageInfoUpdateRequestDto requestDto) {
        User user = getUserByUserId(userId);

        String name = hasText(requestDto.getName()) ? requestDto.getName().trim() : user.getName();
        LocalDate birthDate = requestDto.getBirthDate() != null ? requestDto.getBirthDate() : user.getBirthDate();
        String gender = hasText(requestDto.getGender()) ? requestDto.getGender() : user.getGender();
        String phone = hasText(requestDto.getPhone()) ? requestDto.getPhone().trim() : user.getPhone();
        String address = hasText(requestDto.getAddress()) ? requestDto.getAddress().trim() : user.getAddress();
        String addressDetail = requestDto.getAddressDetail() != null ? requestDto.getAddressDetail().trim() : user.getAddressDetail();
        String email = hasText(requestDto.getEmail()) ? requestDto.getEmail().trim() : user.getEmail();

        String hasVehicle = hasText(requestDto.getHasVehicle()) ? requestDto.getHasVehicle().trim() : "no";
        String vehicleModel = hasText(requestDto.getVehicleModel()) ? requestDto.getVehicleModel().trim() : null;
        String vehicleYear = hasText(requestDto.getVehicleYear()) ? requestDto.getVehicleYear().trim() : null;
        Integer drivingDistance = requestDto.getDrivingDistance();

        if (addressDetail == null) {
            addressDetail = "";
        }

        validateBasicInfo(name, birthDate, gender, phone, address, addressDetail, email);

        if ("yes".equalsIgnoreCase(hasVehicle) || "y".equalsIgnoreCase(hasVehicle)) {
            hasVehicle = "yes";
            validateOwnedVehicleInfo(vehicleModel, vehicleYear, drivingDistance);
        } else {
            hasVehicle = "no";
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

        userRepository.saveAndFlush(user);
    }

    @Override
    public List<MyConsultationResponseDto> getMyConsultations(String userId) {
        User user = getUserByUserId(userId);

        return consultationRepository.findByUserUserIdOrderByCreatedAtDesc(user.getUserId()).stream()
                .map(MyConsultationResponseDto::from)
                .toList();
    }

    @Override
    @Transactional
    public void cancelMyConsultation(String userId, String consultId) {
        User user = getUserByUserId(userId);

        Consultation consultation = consultationRepository.findById(consultId)
                .orElseThrow(() -> new IllegalArgumentException("상담 정보를 찾을 수 없습니다."));

        if (!consultation.getUser().getUserId().equals(user.getUserId())) {
            throw new IllegalArgumentException("본인의 상담만 취소할 수 있습니다.");
        }

        consultation.cancel();
    }

    @Override
    public List<MyInquiryResponseDto> getMyInquiries(String userId) {
        User user = getUserByUserId(userId);

        return inquiryRepository.findByUserUserIdOrderByCreatedAtDesc(user.getUserId()).stream()
                .map(MyInquiryResponseDto::from)
                .toList();
    }

    @Override
    public MyInquiryResponseDto getMyInquiryDetail(String userId, String inquiryId) {
        User user = getUserByUserId(userId);

        Inquiry inquiry = inquiryRepository.findById(inquiryId)
                .orElseThrow(() -> new IllegalArgumentException("문의 정보를 찾을 수 없습니다."));

        if (!inquiry.getUser().getUserId().equals(user.getUserId())) {
            throw new IllegalArgumentException("본인의 문의만 조회할 수 있습니다.");
        }

        return MyInquiryResponseDto.from(inquiry);
    }

    @Override
    @Transactional
    public void withdraw(String userId, WithdrawRequestDto withdrawRequestDto) {
        System.out.println("===== withdraw debug start =====");
        System.out.println("userId = [" + userId + "]");
        System.out.println("password = [" + withdrawRequestDto.getPassword() + "]");
        System.out.println("withdrawReason = [" + withdrawRequestDto.getWithdrawReason() + "]");
        System.out.println("agreeWithdraw = [" + withdrawRequestDto.getAgreeWithdraw() + "]");
        System.out.println("isInvalid = [" + withdrawRequestDto.isInvalid() + "]");

        if (withdrawRequestDto.isInvalid()) {
            System.out.println(">>> invalid request dto");
            throw new IllegalArgumentException("회원탈퇴 입력값을 확인해주세요.");
        }

        User user = getUserByUserId(userId);
        System.out.println("loadedUserId = [" + user.getUserId() + "]");
        System.out.println("loadedUserStatus = [" + user.getUserStatus() + "]");

        if (user.getUserStatus() == UserStatus.WITHDRAWN) {
            System.out.println(">>> already withdrawn user");
            throw new IllegalArgumentException("이미 탈퇴 처리된 회원입니다.");
        }

        boolean hasInProgressConsultation =
                consultationRepository.existsByUserUserIdAndConsultStatus(user.getUserId(), CONSULT_STATUS_IN_PROGRESS);

        System.out.println("hasInProgressConsultation = [" + hasInProgressConsultation + "]");
        System.out.println("CONSULT_STATUS_IN_PROGRESS = [" + CONSULT_STATUS_IN_PROGRESS + "]");

        if (hasInProgressConsultation) {
            System.out.println(">>> blocked by in-progress consultation");
            throw new IllegalArgumentException("현재 진행중인 상담이 있어 탈퇴가 불가능합니다. 상담 완료 또는 취소 후 다시 시도해주세요.");
        }

        boolean passwordMatched = user.getPassword().equals(withdrawRequestDto.getPassword());
        System.out.println("passwordMatched = [" + passwordMatched + "]");

        if (!passwordMatched) {
            System.out.println(">>> password mismatch");
            throw new IllegalArgumentException("비밀번호를 확인하세요.");
        }

        user.withdraw();
        System.out.println(">>> withdraw success");
        System.out.println("===== withdraw debug end =====");
    }

    @Override
    public List<MyWishlistResponseDto> getMyWishlist(String userId) {
        getUserByUserId(userId);

        return List.of(
                MyWishlistResponseDto.builder()
                        .wishlistId("wish0001")
                        .brand("현대")
                        .modelName("아이오닉 5")
                        .vehicleClass("중형 SUV")
                        .priceBasic(5200)
                        .imageUrl("/images/ev_HYUNDAI_IONIQ5.png")
                        .detailUrl("/vehicle/detail?vehicleId=vehicle0001")
                        .build(),
                MyWishlistResponseDto.builder()
                        .wishlistId("wish0002")
                        .brand("기아")
                        .modelName("EV6")
                        .vehicleClass("중형 SUV")
                        .priceBasic(4870)
                        .imageUrl("/images/ev_KIA_EV6.png")
                        .detailUrl("/vehicle/detail?vehicleId=vehicle0002")
                        .build(),
                MyWishlistResponseDto.builder()
                        .wishlistId("wish0003")
                        .brand("현대")
                        .modelName("아이오닉 9")
                        .vehicleClass("대형 SUV")
                        .priceBasic(6700)
                        .imageUrl("/images/ev_HYUNDAI_IONIQ9.png")
                        .detailUrl("/vehicle/detail?vehicleId=vehicle0003")
                        .build()
        );
    }

    @Override
    @Transactional
    public void deleteWishlist(String userId, String wishlistId) {
        getUserByUserId(userId);

        if (wishlistId == null || wishlistId.isBlank()) {
            throw new IllegalArgumentException("관심차량 식별값이 없습니다.");
        }
    }

    private User getUserByUserId(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("회원정보를 찾을 수 없습니다."));
    }

    private void validateBasicInfo(
            String name,
            LocalDate birthDate,
            String gender,
            String phone,
            String address,
            String addressDetail,
            String email
    ) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("이름을 입력해주세요.");
        }

        if (birthDate == null) {
            throw new IllegalArgumentException("생년월일을 입력해주세요.");
        }

        if (gender == null || gender.isBlank()) {
            throw new IllegalArgumentException("성별 정보를 확인해주세요.");
        }

        if (phone == null || phone.isBlank()) {
            throw new IllegalArgumentException("전화번호를 입력해주세요.");
        }

        if (address == null || address.isBlank()) {
            throw new IllegalArgumentException("주소를 입력해주세요.");
        }

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("이메일을 입력해주세요.");
        }
    }

    private void validateOwnedVehicleInfo(String vehicleModel, String vehicleYear, Integer drivingDistance) {
        if (vehicleModel == null || vehicleModel.isBlank()) {
            throw new IllegalArgumentException("보유 차량명을 입력해주세요.");
        }

        if (vehicleYear == null || vehicleYear.isBlank()) {
            throw new IllegalArgumentException("보유차량 연식을 입력해주세요.");
        }

        if (!vehicleYear.matches("^\\d{4}$")) {
            throw new IllegalArgumentException("보유차량 연식은 4자리 숫자(YYYY)로 입력해주세요.");
        }

        int currentYear = Year.now(KOREA_ZONE_ID).getValue();
        int year = Integer.parseInt(vehicleYear);

        if (year < MIN_VEHICLE_YEAR) {
            throw new IllegalArgumentException("보유차량 연식이 올바르지 않습니다.");
        }

        if (year > currentYear) {
            throw new IllegalArgumentException("보유차량 연식은 현재 연도보다 클 수 없습니다.");
        }

        if (drivingDistance == null) {
            throw new IllegalArgumentException("주행거리를 입력해주세요.");
        }

        if (drivingDistance < 0) {
            throw new IllegalArgumentException("주행거리는 0 이상이어야 합니다.");
        }
    }

    private boolean hasText(String value) {
        return value != null && !value.trim().isEmpty();
    }
}