package com.evcar.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/vehicle")
public class AdminVehicleController {

    /**
     * 차량 목록 (임시)
     */
    @GetMapping("")
    public String vehicleList() {
        return "admin/vehicle/list";
    }

    /**
     * 차량 등록
     */
    @GetMapping("/form")
    public String vehicleForm(Model model) {

        model.addAttribute("isEditMode", false);

        return "admin/vehicle/form";
    }

    /**
     * 차량 수정
     */
    @GetMapping("/{id}/form")
    public String vehicleEditForm(@PathVariable Long id, Model model) {

        model.addAttribute("isEditMode", true);

        return "admin/vehicle/form";
    }
}