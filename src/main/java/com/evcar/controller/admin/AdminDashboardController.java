<<<<<<< HEAD
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
        return "admin/dashboard";
    }
=======
package com.evcar.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminDashboardController {

    @GetMapping("")
    public String dashboard() {
        return "admin/dashboard";
    }
>>>>>>> ccef92c2bb418a9ae53ae1d629a62927994a692e
}