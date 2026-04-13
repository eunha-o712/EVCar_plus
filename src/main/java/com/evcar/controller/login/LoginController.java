package com.evcar.controller.login;

import com.evcar.domain.user.User;
import com.evcar.domain.user.UserRole;
import com.evcar.dto.login.IdRecoveryDto;
import com.evcar.dto.login.LoginRequestDto;
import com.evcar.dto.login.PasswordResetDto;
import com.evcar.service.login.LoginService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/login")
public class LoginController {

    private final LoginService loginService;

    @GetMapping
    public String loginPage(@RequestParam(value = "error", required = false) String error,
                            Model model) {

        model.addAttribute("loginRequestDto", new LoginRequestDto());

        if (!model.containsAttribute("errorMessage") && error != null) {
            model.addAttribute("errorMessage", "아이디 또는 비밀번호가 올바르지 않습니다.");
        }

        return "login/login";
    }

    @GetMapping("/main")
    public String index() {
        return "main/index";
    }

    @PostMapping
    public String login(@ModelAttribute LoginRequestDto dto,
                        HttpSession session,
                        RedirectAttributes redirectAttributes) {
        try {
            User loginUser = loginService.login(dto);

            if (loginUser == null) {
                redirectAttributes.addFlashAttribute("errorMessage", "아이디 또는 비밀번호가 올바르지 않습니다.");
                return "redirect:/login";
            }

            session.setAttribute("loginUser", loginUser);

            if (loginUser.getRole() == UserRole.ADMIN) {
                return "redirect:/admin";
            }

            return "redirect:/login/main";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/login";
        }
    }

    @GetMapping("/find-id")
    public String idRecoveryPage(Model model) {
        model.addAttribute("idRecoveryDto", new IdRecoveryDto());
        return "login/idrecovery";
    }

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

    @GetMapping("/reset-pw")
    public String pwResetPage(Model model) {
        model.addAttribute("passwordResetDto", new PasswordResetDto());
        return "login/pwreset";
    }

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

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

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