package com.evcar.controller.vehicle;

import java.util.List;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.evcar.dto.vehicle.VehicleListDto;
import com.evcar.service.vehicle.WishlistService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/wishlist")
public class WishlistController {

    private final WishlistService wishlistService;

    @PostMapping("/add")
    public String add(@RequestParam Long vehicleId) {   
        wishlistService.add(vehicleId);
        return "ok";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam Long vehicleId) {  
        wishlistService.remove(vehicleId);
        return "ok";
    }
    
    @GetMapping("/list")
    public String wishlistPage(Model model) {

        List<VehicleListDto> list = wishlistService.getWishlistVehicles();

        model.addAttribute("vehicleList", list);

        return "wishlist/list";
    }
}