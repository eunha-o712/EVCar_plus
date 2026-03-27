package com.evcar.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/stats")
public class AdminStatsController {

    @GetMapping("")
    public String statsDashboard() {
        return "admin/stats/dashboard";
    }
}