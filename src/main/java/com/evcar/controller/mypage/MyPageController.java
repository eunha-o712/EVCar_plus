package com.evcar.controller.mypage;

import com.evcar.dto.mypage.MyConsultationResponseDto;
import com.evcar.dto.mypage.MyInquiryResponseDto;
import com.evcar.dto.mypage.MyPageInfoResponseDto;
import com.evcar.dto.mypage.MyPageInfoUpdateRequestDto;
import com.evcar.dto.mypage.WithdrawRequestDto;
import com.evcar.service.mypage.MyPageService;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/mypage")
@RequiredArgsConstructor
public class MyPageController {

    private final MyPageService myPageService;

    @GetMapping
    public String myPageRedirect() {
        return "redirect:/mypage/main";
    }

    @GetMapping("/main")
    public String myPageMain(HttpSession session, Model model) {
        Integer loginUserId = getLoginUserId(session);
        MyPageInfoResponseDto myPageInfo = myPageService.getMyPageInfo(loginUserId);

        model.addAttribute("myPageInfo", myPageInfo);
        return "mypage/myPageMain";
    }

    @GetMapping("/info")
    public String myInfo(HttpSession session, Model model) {
        Integer loginUserId = getLoginUserId(session);
        MyPageInfoResponseDto myPageInfo = myPageService.getMyPageInfo(loginUserId);

        model.addAttribute("myPageInfo", myPageInfo);
        model.addAttribute("myPageInfoUpdateRequestDto", toUpdateRequestDto(myPageInfo));

        return "mypage/myInfo";
    }

    @PostMapping("/info")
    public String updateMyInfo(
            HttpSession session,
            @ModelAttribute MyPageInfoUpdateRequestDto myPageInfoUpdateRequestDto
    ) {
        Integer loginUserId = getLoginUserId(session);
        myPageService.updateMyPageInfo(loginUserId, myPageInfoUpdateRequestDto);

        return "redirect:/mypage/info";
    }

    @GetMapping("/wishlist")
    public String myWishlist() {
        return "mypage/myWishlist";
    }

    
    

    @GetMapping("/consultation")
    public String myConsultation(HttpSession session, Model model) {
        Integer loginUserId = getLoginUserId(session);
        List<MyConsultationResponseDto> consultations = myPageService.getMyConsultations(loginUserId);

        model.addAttribute("consultations", consultations);
        return "mypage/myConsultation";
    }

    @PostMapping("/consultation/cancel")
    public String cancelMyConsultation(
            HttpSession session,
            @RequestParam("consultId") Integer consultId
    ) {
        Integer loginUserId = getLoginUserId(session);
        myPageService.cancelMyConsultation(loginUserId, consultId);

        return "redirect:/mypage/consultation";
    }

    @GetMapping("/inquiry")
    public String myInquiry(HttpSession session, Model model) {
        Integer loginUserId = getLoginUserId(session);
        List<MyInquiryResponseDto> inquiries = myPageService.getMyInquiries(loginUserId);

        model.addAttribute("inquiries", inquiries);
        return "mypage/myInquiry";
    }

    @GetMapping("/withdraw")
    public String myWithdraw(HttpSession session, Model model) {
        Integer loginUserId = getLoginUserId(session);
        MyPageInfoResponseDto myPageInfo = myPageService.getMyPageInfo(loginUserId);

        model.addAttribute("myPageInfo", myPageInfo);
        model.addAttribute("withdrawRequestDto", WithdrawRequestDto.builder().build());

        return "mypage/myWithdraw";
    }

    @PostMapping("/withdraw")
    public String withdraw(
            HttpSession session,
            @ModelAttribute WithdrawRequestDto withdrawRequestDto
    ) {
        Integer loginUserId = getLoginUserId(session);
        myPageService.withdraw(loginUserId, withdrawRequestDto);

        session.invalidate();
        return "redirect:/";
    }

    private Integer getLoginUserId(HttpSession session) {
        Object loginUserId = session.getAttribute("loginUserId");

        if (loginUserId == null) {
            return 1;
        }

        if (loginUserId instanceof Integer userId) {
            return userId;
        }

        if (loginUserId instanceof Long userId) {
            return userId.intValue();
        }

        if (loginUserId instanceof String userId) {
            return Integer.parseInt(userId);
        }

        throw new IllegalArgumentException("로그인 사용자 정보 형식이 올바르지 않습니다.");
    }

    private MyPageInfoUpdateRequestDto toUpdateRequestDto(MyPageInfoResponseDto responseDto) {
        return MyPageInfoUpdateRequestDto.builder()
                .name(responseDto.getName())
                .birthDate(responseDto.getBirthDate())
                .gender(responseDto.getGender())
                .phone(responseDto.getPhone())
                .address(responseDto.getAddress())
                .addressDetail(responseDto.getAddressDetail())
                .email(responseDto.getEmail())
                .hasVehicle(responseDto.getHasVehicle())
                .vehicleModel(responseDto.getVehicleModel())
                .vehicleYear(responseDto.getVehicleYear())
                .drivingDistance(responseDto.getDrivingDistance())
                .build();
    }
}