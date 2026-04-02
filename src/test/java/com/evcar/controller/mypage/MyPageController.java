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
        String loginId = getLoginId(session);
        MyPageInfoResponseDto myPageInfo = myPageService.getMyPageInfo(loginId);

        model.addAttribute("myPageInfo", myPageInfo);
        return "mypage/myPageMain";
    }

    @GetMapping("/info")
    public String myInfo(HttpSession session, Model model) {
        String loginId = getLoginId(session);
        MyPageInfoResponseDto myPageInfo = myPageService.getMyPageInfo(loginId);

        model.addAttribute("myPageInfo", myPageInfo);
        model.addAttribute("myPageInfoUpdateRequestDto", toUpdateRequestDto(myPageInfo));

        return "mypage/myInfo";
    }

    @PostMapping("/info")
    public String updateMyInfo(
            HttpSession session,
            @ModelAttribute MyPageInfoUpdateRequestDto myPageInfoUpdateRequestDto
    ) {
        String loginId = getLoginId(session);
        myPageService.updateMyPageInfo(loginId, myPageInfoUpdateRequestDto);

        return "redirect:/mypage/info";
    }

    @GetMapping("/wishlist")
    public String myWishlist() {
        return "mypage/myWishlist";
    }

    @GetMapping("/consultation")
    public String myConsultation(HttpSession session, Model model) {
        String loginId = getLoginId(session);
        List<MyConsultationResponseDto> consultations = myPageService.getMyConsultations(loginId);

        model.addAttribute("consultations", consultations);
        return "mypage/myConsultation";
    }

    @PostMapping("/consultation/cancel")
    public String cancelMyConsultation(
            HttpSession session,
            @RequestParam("consultId") Integer consultId
    ) {
        String loginId = getLoginId(session);
        myPageService.cancelMyConsultation(loginId, consultId);

        return "redirect:/mypage/consultation";
    }

    @GetMapping("/inquiry")
    public String myInquiry(HttpSession session, Model model) {
        String loginId = getLoginId(session);
        List<MyInquiryResponseDto> inquiries = myPageService.getMyInquiries(loginId);

        model.addAttribute("inquiries", inquiries);
        return "mypage/myInquiry";
    }

    @GetMapping("/withdraw")
    public String myWithdraw(HttpSession session, Model model) {
        String loginId = getLoginId(session);
        MyPageInfoResponseDto myPageInfo = myPageService.getMyPageInfo(loginId);

        model.addAttribute("myPageInfo", myPageInfo);
        model.addAttribute("withdrawRequestDto", WithdrawRequestDto.builder().build());

        return "mypage/myWithdraw";
    }

    @PostMapping("/withdraw")
    public String withdraw(
            HttpSession session,
            @ModelAttribute WithdrawRequestDto withdrawRequestDto
    ) {
        String loginId = getLoginId(session);
        myPageService.withdraw(loginId, withdrawRequestDto);

        session.invalidate();
        return "redirect:/";
    }

    private String getLoginId(HttpSession session) {
        Object loginUserId = session.getAttribute("loginUserId");

        if (loginUserId == null) {
            session.setAttribute("loginUserId", "seohyunryu");
            return "seohyunryu"; //화면 확인용임시로그인
        }
        /*
        if (loginUserId == null) {
            throw new IllegalArgumentException("로그인이 필요합니다.");
        }
	*/
        return String.valueOf(loginUserId);
    }

    private MyPageInfoUpdateRequestDto toUpdateRequestDto(MyPageInfoResponseDto responseDto) {
        return MyPageInfoUpdateRequestDto.builder()
                .name(responseDto.getName())
                .birthDate(responseDto.getBirthDate() != null
                        ? responseDto.getBirthDate()
                        : java.time.LocalDate.of(1990, 1, 1))
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