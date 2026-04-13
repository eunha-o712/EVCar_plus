package com.evcar.controller.vehicle;

import com.evcar.dto.vehicle.VehicleDetailDto;
import com.evcar.dto.vehicle.VehicleImageResponseDto;
import com.evcar.dto.vehicle.VehicleListDto;
import com.evcar.service.vehicle.VehicleImageService;
import com.evcar.service.vehicle.VehicleService;
import com.evcar.service.wishlist.WishlistService;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/vehicle")
public class VehicleController {

    private final VehicleService vehicleService;
    private final WishlistService wishlistService;
    private final VehicleImageService vehicleImageService;

    @GetMapping
    public String vehicleHome(Model model) {
        List<VehicleListDto> vehicleList = vehicleService.getVehicleList(null, null);

        model.addAttribute("vehicleList", vehicleList);
        model.addAttribute("selectedBrand", "전체");
        model.addAttribute("selectedClass", "전체");
        model.addAttribute("totalCount", vehicleList.size());
        model.addAttribute("totalPages", 1);
        model.addAttribute("currentPage", 0);

        return "vehicle/list";
    }

    @GetMapping("/list")
    public String vehicleList(@RequestParam(name = "brand", defaultValue = "전체") String brand,
                              @RequestParam(name = "vehicleClass", defaultValue = "전체") String vehicleClass,
                              Model model) {

        String serviceBrand = convertBrandToCode(brand);
        String serviceVehicleClass = convertVehicleClass(vehicleClass);

        List<VehicleListDto> vehicleList = vehicleService.getVehicleList(serviceBrand, serviceVehicleClass);

        model.addAttribute("vehicleList", vehicleList);
        model.addAttribute("selectedBrand", brand);
        model.addAttribute("selectedClass", vehicleClass);
        model.addAttribute("totalCount", vehicleList.size());
        model.addAttribute("totalPages", 1);
        model.addAttribute("currentPage", 0);

        return "vehicle/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable("id") String id,
                         HttpSession session,
                         Model model) {
        VehicleDetailDto dto = vehicleService.getDetail(id);

        Object userId = session.getAttribute("userId");
        if (userId != null) {
            dto.setWished(wishlistService.isWished(String.valueOf(userId), id));
        } else {
            dto.setWished(false);
        }

        List<VehicleImageResponseDto> images = vehicleImageService.getImages(id);

        model.addAttribute("vehicle", dto);
        model.addAttribute("images", images);

        return "vehicle/detail";
    }

    private String convertBrandToCode(String brand) {
        if (brand == null || brand.isBlank() || "전체".equals(brand)) {
            return null;
        }

        return switch (brand) {
            case "현대" -> "HYUNDAI";
            case "기아" -> "KIA";
            default -> brand;
        };
    }

    private String convertVehicleClass(String vehicleClass) {
        if (vehicleClass == null || vehicleClass.isBlank() || "전체".equals(vehicleClass)) {
            return null;
        }

        return vehicleClass;
    }
}