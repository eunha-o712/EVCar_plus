package com.evcar.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/faq")
public class AdminFaqController {

    @GetMapping("")
    public String faqList() {
        return "admin/faq/list";
    }

    @GetMapping("/form")
    public String faqForm() {
        return "admin/faq/form";
    }
}