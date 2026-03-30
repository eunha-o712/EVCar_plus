package com.evcar.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/vehicle")
public class AdminVehicleController {

    @GetMapping("")
    public String vehicleList(Model model) {
        model.addAttribute("vehicleList", null);
        return "admin/vehicle/list";
    }

    @GetMapping("/form")
    public String vehicleForm(Model model) {
        model.addAttribute("isEditMode", false);
        return "admin/vehicle/form";
    }

    @GetMapping("/{id}/form")
    public String vehicleEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("isEditMode", true);
        return "admin/vehicle/form";
    }
}