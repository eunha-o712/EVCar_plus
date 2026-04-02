package com.evcar.controller.user;

import com.evcar.dto.user.UserSignupDto;
import com.evcar.service.user.UserService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/signup")
public class UserController {

    private final UserService userService;

    @GetMapping("/term")
    public String termsPage() {
        return "user/agreement";
    }

    @GetMapping
    public String signupPage(@RequestParam(name = "agreePrivacy", required = false) Boolean agreePrivacy,
                             @RequestParam(name = "agreeTerms", required = false) Boolean agreeTerms,
                             Model model) {

        model.addAttribute("userSignupDto", new UserSignupDto());
        return "user/signup";
    }

    @PostMapping
    public String signup(@ModelAttribute UserSignupDto dto,
                         Model model,
                         HttpSession session) {

        userService.signup(dto);

        session.setAttribute("signupUser", dto); // 🔥 여기 추가

        return "redirect:/signup/complete";

    }
    
    @GetMapping("/check-id")
    @ResponseBody
    public String checkId(@RequestParam("loginId") String loginId) {

        boolean exists = userService.isUserLoginIdDuplicate(loginId);

        return exists ? "true" : "false";
    }

    @GetMapping("/check-email")
    @ResponseBody
    public String checkEmail(@RequestParam("email") String email) {

        boolean exists = userService.isUserEmailDuplicate(email);

        return exists ? "true" : "false";
    }
    
    @GetMapping("/complete")
    public String signupComplete(HttpSession session, Model model) {

        Object user = session.getAttribute("signupUser");

        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("signupUser", user);
        session.removeAttribute("signupUser");

        return "user/signupcomplete";
    }
}