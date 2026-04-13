package com.evcar.controller.admin;

import com.evcar.dto.admin.AdminDashboardResponseDto;
import com.evcar.service.admin.AdminDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminDashboardController {

    private final AdminDashboardService adminDashboardService;

    @GetMapping("")
    public String dashboard(Model model) {
        AdminDashboardResponseDto dashboard = adminDashboardService.getDashboardData();
        model.addAttribute("dashboard", dashboard);
        return "admin/dashboard/main-dashboard";
    }

}