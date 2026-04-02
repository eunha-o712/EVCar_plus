package com.evcar.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/inquiry")
public class AdminInquiryController {

    @GetMapping("")
    public String inquiryList() {
        return "admin/inquiry/list";
    }
}