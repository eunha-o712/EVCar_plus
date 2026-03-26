package com.evcar.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/user")
public class AdminUserController {

    @GetMapping("")
    public String userList(Model model) {

        model.addAttribute("userList", null);
        model.addAttribute("selectedUser", null);

        return "admin/user/list";
    }
}