package com.evcar.controller.login;

import com.evcar.domain.user.User;
import com.evcar.dto.login.IdRecoveryDto;
import com.evcar.dto.login.LoginRequestDto;
import com.evcar.dto.login.PasswordResetDto;
import com.evcar.service.login.LoginService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/login")
public class LoginController {

    private final LoginService loginService;

    // 로그인 화면
    @GetMapping
    public String loginPage(@RequestParam(value = "error", required = false) String error,
                            Model model) {

        model.addAttribute("loginRequestDto", new LoginRequestDto());

        if (error != null) {
            model.addAttribute("errorMessage", "아이디 또는 비밀번호가 올바르지 않습니다.");
        }

        return "login/login";
    }

    // 로그인 성공 후 메인 페이지 이동
    @GetMapping("/main")
    public String index() {
    	return "main/index";
    }
 // 로그인 처리
    @PostMapping
    public String login(@ModelAttribute LoginRequestDto dto,
                        HttpSession session) {
        try {
        	User loginUser = loginService.login(dto);

        	if (loginUser == null) {
        	    return "redirect:/login";
        	}

        	session.setAttribute("loginUser", loginUser);
        	return "redirect:/login/main";
        } catch (IllegalArgumentException e) {
            return "redirect:/login?error=true";
        }
    }

    // 아이디 찾기 화면
    @GetMapping("/find-id")
    public String idRecoveryPage(Model model) {
        model.addAttribute("idRecoveryDto", new IdRecoveryDto());
        return "login/idrecovery";
    }

    // 아이디 찾기 처리
    @PostMapping("/find-id")
    public String findUserLoginId(@ModelAttribute IdRecoveryDto dto, Model model) {
        try {
            String foundId = loginService.findUserLoginId(dto);
            model.addAttribute("foundId", foundId);
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
        }
        return "login/idrecovery";
    }

    // 비밀번호 재설정 화면
    @GetMapping("/reset-pw")
    public String pwResetPage(Model model) {
        model.addAttribute("passwordResetDto", new PasswordResetDto());
        return "login/pwreset";
    }
    // 비밀번호 검증
    @PostMapping("/reset-pw")
    public String resetPassword(@RequestParam("loginId") String loginId,
                                @RequestParam("name") String name,
                                @RequestParam("email") String email,
                                Model model) {

        try {
            PasswordResetDto dto = PasswordResetDto.builder()
                    .loginId(loginId)
                    .name(name)
                    .email(email)
                    .build();

            loginService.verifyUser(dto);

            model.addAttribute("passwordResetDto",
                    PasswordResetDto.builder()
                            .loginId(loginId)
                            .name(name)
                            .email(email)
                            .build());

            return "login/pwreset";

        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "login/idrecovery";
        }
    }

    // 로그아웃
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
    // 비밀번호 변경
    @PostMapping("/change-pw")
    public String changePassword(@ModelAttribute PasswordResetDto dto, Model model) {

        try {
            loginService.resetPassword(dto);

            model.addAttribute("successMessage", "비밀번호가 변경되었습니다.");

            return "login/pwreset";

        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "login/pwreset";
        }
    }
}