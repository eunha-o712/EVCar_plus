<<<<<<< HEAD
package com.evcar.controller.mypage;

import com.evcar.domain.user.UserStatus;
import com.evcar.dto.mypage.MyConsultationResponseDto;
import com.evcar.dto.mypage.MyInquiryResponseDto;
import com.evcar.dto.mypage.MyPageInfoResponseDto;
import com.evcar.dto.mypage.MyPageInfoUpdateRequestDto;
import com.evcar.dto.mypage.MyPageSummaryResponseDto;
import com.evcar.dto.mypage.MyWishlistResponseDto;
import com.evcar.dto.mypage.WithdrawRequestDto;
import com.evcar.service.mypage.MyPageService;
import jakarta.servlet.http.HttpSession;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/mypage")
@RequiredArgsConstructor
public class MyPageController {

    private static final String DEV_PREVIEW_USER_ID = "user0001";
    private static final String ACCESS_DENIED_VIEW = "mypage/memberAccessDenied";

    private final MyPageService myPageService;

    @GetMapping
    public String myPageRedirect() {
        return "redirect:/mypage/main";
    }

    @GetMapping("/main")
    public String myPageMain(
            @RequestParam(value = "previewUserId", required = false) String previewUserId,
            HttpSession session,
            Model model
    ) {
        MyPageInfoResponseDto myPageInfo = getAccessibleMyPageInfo(session, previewUserId, model);
        if (myPageInfo == null) {
            return ACCESS_DENIED_VIEW;
        }

        MyPageSummaryResponseDto summary = myPageService.getMyPageSummary(myPageInfo.getUserId());

        model.addAttribute("currentUserId", myPageInfo.getUserId());
        model.addAttribute("myPageInfo", myPageInfo);
        model.addAttribute("summary", summary);
        return "mypage/myPageMain";
    }

    @GetMapping("/info")
    public String myInfo(
            @RequestParam(value = "previewUserId", required = false) String previewUserId,
            HttpSession session,
            Model model
    ) {
        MyPageInfoResponseDto myPageInfo = getAccessibleMyPageInfo(session, previewUserId, model);
        if (myPageInfo == null) {
            return ACCESS_DENIED_VIEW;
        }

        model.addAttribute("currentUserId", myPageInfo.getUserId());
        model.addAttribute("myPageInfo", myPageInfo);
        model.addAttribute("myPageInfoUpdateRequestDto", toUpdateRequestDto(myPageInfo));
        return "mypage/myInfo";
    }

    @PostMapping("/info")
    public String updateMyInfo(
            HttpSession session,
            @ModelAttribute MyPageInfoUpdateRequestDto myPageInfoUpdateRequestDto,
            Model model
    ) {
        MyPageInfoResponseDto myPageInfo = getAccessibleMyPageInfo(session, null, model);
        if (myPageInfo == null) {
            return ACCESS_DENIED_VIEW;
        }

        myPageService.updateMyPageInfo(myPageInfo.getUserId(), myPageInfoUpdateRequestDto);
        return "redirect:/mypage/info";
    }

    @GetMapping("/wishlist")
    public String myWishlist(
            @RequestParam(value = "previewUserId", required = false) String previewUserId,
            HttpSession session,
            Model model
    ) {
        MyPageInfoResponseDto myPageInfo = getAccessibleMyPageInfo(session, previewUserId, model);
        if (myPageInfo == null) {
            return ACCESS_DENIED_VIEW;
        }

        model.addAttribute("currentUserId", myPageInfo.getUserId());
        model.addAttribute("myPageInfo", myPageInfo);
        return "mypage/myWishlist";
    }

    @GetMapping("/wishlist/api")
    @ResponseBody
    public List<MyWishlistResponseDto> getMyWishlistApi(
            @RequestParam(value = "previewUserId", required = false) String previewUserId,
            HttpSession session,
            Model model
    ) {
        MyPageInfoResponseDto myPageInfo = getAccessibleMyPageInfo(session, previewUserId, model);
        if (myPageInfo == null) {
            return Collections.emptyList();
        }

        return myPageService.getMyWishlist(myPageInfo.getUserId());
    }

    @PostMapping("/wishlist/delete")
    @ResponseBody
    public void deleteWishlist(
            HttpSession session,
            @RequestParam("wishlistId") String wishlistId,
            Model model
    ) {
        MyPageInfoResponseDto myPageInfo = getAccessibleMyPageInfo(session, null, model);
        if (myPageInfo == null) {
            throw new IllegalArgumentException("사용자 정보가 없습니다.");
        }

        myPageService.deleteWishlist(myPageInfo.getUserId(), wishlistId);
    }

    @GetMapping("/consultation")
    public String myConsultation(
            @RequestParam(value = "previewUserId", required = false) String previewUserId,
            HttpSession session,
            Model model
    ) {
        MyPageInfoResponseDto myPageInfo = getAccessibleMyPageInfo(session, previewUserId, model);
        if (myPageInfo == null) {
            return ACCESS_DENIED_VIEW;
        }

        List<MyConsultationResponseDto> consultations = myPageService.getMyConsultations(myPageInfo.getUserId());
        model.addAttribute("currentUserId", myPageInfo.getUserId());
        model.addAttribute("consultations", consultations);
        return "mypage/myConsultation";
    }

    @PostMapping("/consultation/cancel")
    public String cancelMyConsultation(
            HttpSession session,
            @RequestParam("consultId") String consultId,
            Model model
    ) {
        MyPageInfoResponseDto myPageInfo = getAccessibleMyPageInfo(session, null, model);
        if (myPageInfo == null) {
            return ACCESS_DENIED_VIEW;
        }

        myPageService.cancelMyConsultation(myPageInfo.getUserId(), consultId);
        return "redirect:/mypage/consultation";
    }

    @GetMapping("/inquiry")
    public String myInquiry(
            @RequestParam(value = "previewUserId", required = false) String previewUserId,
            HttpSession session,
            Model model
    ) {
        MyPageInfoResponseDto myPageInfo = getAccessibleMyPageInfo(session, previewUserId, model);
        if (myPageInfo == null) {
            return ACCESS_DENIED_VIEW;
        }

        List<MyInquiryResponseDto> inquiries = myPageService.getMyInquiries(myPageInfo.getUserId());
        model.addAttribute("currentUserId", myPageInfo.getUserId());
        model.addAttribute("inquiries", inquiries);
        return "mypage/myInquiry";
    }

    @GetMapping("/inquiry/{inquiryId}")
    public String myInquiryDetail(
            @PathVariable("inquiryId") String inquiryId,
            @RequestParam(value = "previewUserId", required = false) String previewUserId,
            HttpSession session,
            Model model
    ) {
        MyPageInfoResponseDto myPageInfo = getAccessibleMyPageInfo(session, previewUserId, model);
        if (myPageInfo == null) {
            return ACCESS_DENIED_VIEW;
        }

        MyInquiryResponseDto inquiry = myPageService.getMyInquiryDetail(myPageInfo.getUserId(), inquiryId);
        model.addAttribute("currentUserId", myPageInfo.getUserId());
        model.addAttribute("inquiry", inquiry);
        return "mypage/myInquiryDetail";
    }

    @GetMapping("/withdraw")
    public String myWithdraw(
            @RequestParam(value = "previewUserId", required = false) String previewUserId,
            HttpSession session,
            Model model
    ) {
        MyPageInfoResponseDto myPageInfo = getAccessibleMyPageInfo(session, previewUserId, model);
        if (myPageInfo == null) {
            return ACCESS_DENIED_VIEW;
        }

        model.addAttribute("currentUserId", myPageInfo.getUserId());
        model.addAttribute("myPageInfo", myPageInfo);
        model.addAttribute("withdrawRequestDto", WithdrawRequestDto.builder().build());
        return "mypage/myWithdraw";
    }

    @PostMapping("/withdraw")
    public String withdraw(
            HttpSession session,
            @ModelAttribute WithdrawRequestDto withdrawRequestDto,
            Model model
    ) {
        MyPageInfoResponseDto myPageInfo = getAccessibleMyPageInfo(session, null, model);
        if (myPageInfo == null) {
            return ACCESS_DENIED_VIEW;
        }

        myPageService.withdraw(myPageInfo.getUserId(), withdrawRequestDto);
        session.invalidate();

        model.addAttribute("message", "회원탈퇴가 완료되었습니다.");
        return ACCESS_DENIED_VIEW;
    }

    private MyPageInfoResponseDto getAccessibleMyPageInfo(
            HttpSession session,
            String previewUserId,
            Model model
    ) {
        String userId = getUserId(session, previewUserId);

        MyPageInfoResponseDto myPageInfo;
        try {
            myPageInfo = myPageService.getMyPageInfo(userId);
        } catch (IllegalArgumentException e) {
            model.addAttribute("message", "존재하지 않는 회원입니다.");
            return null;
        }

        if (UserStatus.WITHDRAWN.name().equalsIgnoreCase(myPageInfo.getUserStatus())) {
            session.invalidate();
            model.addAttribute("message", "탈퇴한 회원은 마이페이지를 이용할 수 없습니다.");
            return null;
        }

        return myPageInfo;
    }

    private String getUserId(HttpSession session, String previewUserId) {
        if (previewUserId != null && !previewUserId.trim().isEmpty()) {
            return previewUserId.trim();
        }

        Object loginUser = session.getAttribute("loginUser");
        if (loginUser instanceof com.evcar.domain.user.User user) {
            return user.getUserId();
        }

        return DEV_PREVIEW_USER_ID;
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
=======
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
>>>>>>> ccef92c2bb418a9ae53ae1d629a62927994a692e
}