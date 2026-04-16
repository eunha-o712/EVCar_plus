package com.evcar.controller.admin;

import com.evcar.dto.admin.AdminVehicleFormResponseDto;
import com.evcar.dto.admin.AdminVehicleSaveRequestDto;
import com.evcar.service.admin.AdminVehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/vehicle")
public class AdminVehicleController {

    private final AdminVehicleService adminVehicleService;

    @GetMapping("")
    public String vehicleList(
            @RequestParam(name = "status", required = false) String status,
            @RequestParam(name = "keyword", required = false) String keyword,
            Model model
    ) {
        model.addAttribute("selectedStatus", status);
        model.addAttribute("keyword", keyword);
        model.addAttribute("vehicleList", adminVehicleService.getVehicleList(status, keyword));
        return "admin/vehicle/list";
    }

    @GetMapping("/form")
    public String vehicleForm(Model model) {
        model.addAttribute("isEditMode", false);
        model.addAttribute("previewVehicleId", adminVehicleService.previewNextVehicleId(null));
        model.addAttribute("vehicleForm", AdminVehicleFormResponseDto.builder()
                .vehicleStatus("ACTIVE")
                .chargingMethod("DC_COMBO")
                .build());
        return "admin/vehicle/form";
    }

    @GetMapping("/{vehicleId}/form")
    public String vehicleEditForm(
            @PathVariable("vehicleId") String vehicleId,
            Model model
    ) {
        model.addAttribute("isEditMode", true);
        model.addAttribute("previewVehicleId", vehicleId);
        model.addAttribute("vehicleForm", adminVehicleService.getVehicleForm(vehicleId));
        return "admin/vehicle/form";
    }

    @ResponseBody
    @GetMapping(value = "/next-id", produces = MediaType.TEXT_PLAIN_VALUE)
    public String nextVehicleId(@RequestParam(name = "brand", required = false) String brand) {
        return adminVehicleService.previewNextVehicleId(brand);
    }

    @PostMapping("/form")
    public String saveVehicle(
            @ModelAttribute("vehicleForm") AdminVehicleSaveRequestDto requestDto,
            @RequestParam(name = "imageFile", required = false) MultipartFile imageFile,
            RedirectAttributes redirectAttributes
    ) {
        adminVehicleService.saveVehicle(requestDto, imageFile);
        redirectAttributes.addFlashAttribute(
                "message",
                requestDto.getVehicleId() == null || requestDto.getVehicleId().isBlank()
                        ? "차량 정보가 등록되었습니다."
                        : "차량 정보가 수정되었습니다."
        );
        return "redirect:/admin/vehicle";
    }

    @PostMapping("/{vehicleId}/delete")
    public String deleteVehicle(
            @PathVariable("vehicleId") String vehicleId,
            RedirectAttributes redirectAttributes
    ) {
        adminVehicleService.deleteVehicle(vehicleId);
        redirectAttributes.addFlashAttribute("message", "차량이 삭제되었습니다.");
        return "redirect:/admin/vehicle";
    }
}