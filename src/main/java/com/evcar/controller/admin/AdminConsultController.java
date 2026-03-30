package com.evcar.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/consultation")
public class AdminConsultController {

    @GetMapping("")
    public String consultationList(Model model) {

        model.addAttribute("consultationList", null);
        model.addAttribute("selectedConsultation", null);

        return "admin/consultation/list";
    }
}