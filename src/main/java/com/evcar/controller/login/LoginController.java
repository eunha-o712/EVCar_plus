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
    public String loginPage(Model model) {
        model.addAttribute("loginRequestDto", new LoginRequestDto());
        return "login/login";
    }

    // 로그인 처리
    @PostMapping
    public String login(@ModelAttribute LoginRequestDto dto,
                        HttpSession session,
                        Model model) {
        try {
            User loginUser = loginService.login(dto);
            session.setAttribute("loginUser", loginUser);
            return "redirect:/";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("loginRequestDto", dto); // 🔥 중요 (다시 넣기)
            return "login/login";
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

    // 비밀번호 재설정 처리 (🔥 핵심 수정)
    @PostMapping("/reset-pw")
    public String resetPassword(@ModelAttribute PasswordResetDto dto, Model model) {

        try {
            loginService.resetPassword(dto);

            model.addAttribute("message", "비밀번호가 변경되었습니다.");
            model.addAttribute("loginRequestDto", new LoginRequestDto());
            return "login/login";

        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "login/pwreset";
        }
    }

    // 로그아웃
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}