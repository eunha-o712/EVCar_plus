package com.evcar.controller.vehicle;

import com.evcar.dto.vehicle.VehicleDetailDto;
import com.evcar.dto.vehicle.VehicleListDto;
import com.evcar.dto.vehicle.VehicleImageResponseDto;
import com.evcar.service.vehicle.VehicleService;
import com.evcar.service.wishlist.WishlistService;
import com.evcar.service.vehicle.VehicleImageService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/vehicle")
public class VehicleController {

	private final VehicleService vehicleService;
	private final WishlistService wishlistService;
	private final VehicleImageService vehicleImageService;

	// 전체 목록
	@GetMapping
	public String vehicleHome(Model model) {

		List<VehicleListDto> vehicleList = vehicleService.getVehicleList(null, null);

		model.addAttribute("vehicleList", vehicleList);
		model.addAttribute("selectedBrand", "전체");
		model.addAttribute("selectedClass", "전체");
		model.addAttribute("totalCount", vehicleList.size());

		return "vehicle/list";
	}

	// 🔥 필터 목록 (수정됨)
	@GetMapping("/list")
	public String vehicleList(@RequestParam(name = "brand", defaultValue = "전체") String brand,
			@RequestParam(name = "vehicleClass", defaultValue = "전체") String vehicleClass, Model model) {

		String originalBrand = brand; // 화면 유지용

		if ("전체".equals(brand))
			brand = null;
		if ("전체".equals(vehicleClass))
			vehicleClass = null;

		// 🔥 여기 핵심 (DB 값으로 변환)
		if ("현대".equals(brand))
			brand = "HYUNDAI";
		if ("기아".equals(brand))
			brand = "KIA";

		List<VehicleListDto> vehicleList = vehicleService.getVehicleList(brand, vehicleClass);

		model.addAttribute("vehicleList", vehicleList);
		model.addAttribute("selectedBrand", originalBrand == null ? "전체" : originalBrand);
		model.addAttribute("selectedClass", vehicleClass == null ? "전체" : vehicleClass);
		model.addAttribute("totalCount", vehicleList.size());

		return "vehicle/list";
	}

	// 상세 페이지
	@GetMapping("/{id}")
	public String detail(@PathVariable("id") String id, Model model) {

		VehicleDetailDto dto = vehicleService.getDetail(id);
		dto.setWished(wishlistService.isWished(id));

		List<VehicleImageResponseDto> images = vehicleImageService.getImages(id);

		System.out.println("images size = " + images.size());

		for (VehicleImageResponseDto img : images) {
		    System.out.println("img url = " + img.getImageUrl());
		}

		model.addAttribute("vehicle", dto);
		model.addAttribute("images", images);

		return "vehicle/detail";
	}
}