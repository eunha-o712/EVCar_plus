<<<<<<< HEAD
package com.evcar.controller.user;

import com.evcar.dto.user.UserSignupDto;
import com.evcar.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
    public String signupPage(
            @RequestParam(name = "agreePrivacy", required = false) Boolean agreePrivacy,
            @RequestParam(name = "agreeTerms", required = false) Boolean agreeTerms,
            Model model
    ) {
        model.addAttribute("userSignupDto", UserSignupDto.builder().build());
        model.addAttribute("agreePrivacy", Boolean.TRUE.equals(agreePrivacy));
        model.addAttribute("agreeTerms", Boolean.TRUE.equals(agreeTerms));
        return "user/signup";
    }

    @PostMapping
    public String signup(@ModelAttribute UserSignupDto dto, Model model) {
        try {
            userService.signup(dto);
            model.addAttribute("signupUser", dto);
            return "user/signupcomplete";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("userSignupDto", dto);
            return "user/signup";
        }
    }

    @GetMapping("/check-id")
    @ResponseBody
    public boolean checkDuplicate(@RequestParam("loginId") String loginId) {
        return userService.isUserLoginIdDuplicate(loginId);
    }

    @GetMapping("/check-email")
    @ResponseBody
    public boolean checkEmailDuplicate(@RequestParam("email") String email) {
        return userService.isUserEmailDuplicate(email);
    }
=======
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
>>>>>>> ccef92c2bb418a9ae53ae1d629a62927994a692e
}