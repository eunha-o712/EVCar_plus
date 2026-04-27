package com.evcar.controller.vehicle;

import com.evcar.domain.user.User;
import com.evcar.dto.vehicle.VehicleAiRecommendRequestDto;
import com.evcar.dto.vehicle.VehicleAiRecommendResponseDto;
import com.evcar.dto.vehicle.VehicleAiVehicleDto;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Controller
@RequiredArgsConstructor
@RequestMapping("/vehicle")
public class VehicleController {

    private static final String AI_SERVER_BASE_URL = "http://localhost:8000";

    private final VehicleService vehicleService;
    private final WishlistService wishlistService;
    private final VehicleImageService vehicleImageService;

    @GetMapping
    public String vehicleHome(@RequestParam(name = "brand", defaultValue = "전체") String brand,
                              @RequestParam(name = "vehicleClass", defaultValue = "전체") String vehicleClass,
                              Model model) {
        return renderVehicleList(brand, vehicleClass, model);
    }

    @GetMapping("/list")
    public String vehicleList(@RequestParam(name = "brand", defaultValue = "전체") String brand,
                              @RequestParam(name = "vehicleClass", defaultValue = "전체") String vehicleClass,
                              Model model) {
        return renderVehicleList(brand, vehicleClass, model);
    }

    @GetMapping("/recommend")
    public String recommendForm(Model model) {
        model.addAttribute("recommendReply", null);
        model.addAttribute("selectedMessage", null);
        return "vehicle/recommend";
    }

    @PostMapping("/recommend")
    public String recommend(@RequestParam("userMessage") String userMessage,
                            Model model) {
        List<VehicleListDto> vehicleList = vehicleService.getVehicleList(null, null);

        VehicleAiRecommendRequestDto requestDto = VehicleAiRecommendRequestDto.builder()
                .userMessage(userMessage)
                .vehicles(toAiVehicles(vehicleList))
                .build();

        try {
            VehicleAiRecommendResponseDto responseDto = RestClient.create(AI_SERVER_BASE_URL)
                    .post()
                    .uri("/ai/recommend")
                    .body(requestDto)
                    .retrieve()
                    .body(VehicleAiRecommendResponseDto.class);

            model.addAttribute("recommendReply", responseDto != null ? responseDto.getReply() : "추천 결과를 가져오지 못했습니다.");
        } catch (RestClientException e) {
            model.addAttribute("recommendReply", "AI 추천 서버 연결에 실패했습니다. AI 서버가 실행 중인지 확인해 주세요.");
        }

        model.addAttribute("selectedMessage", userMessage);

        return "vehicle/recommend";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable("id") String id,
                         HttpSession session,
                         Model model) {
        VehicleDetailDto dto = vehicleService.getDetail(id);

        String userId = getUserId(session);
        if (userId != null) {
            dto.setWished(wishlistService.isWished(userId, id));
        } else {
            dto.setWished(false);
        }

        List<VehicleImageResponseDto> images = vehicleImageService.getImages(id);

        model.addAttribute("vehicle", dto);
        model.addAttribute("images", images);

        return "vehicle/detail";
    }

    private String renderVehicleList(String brand, String vehicleClass, Model model) {
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

    private List<VehicleAiVehicleDto> toAiVehicles(List<VehicleListDto> vehicleList) {
        return vehicleList.stream()
                .map(vehicle -> VehicleAiVehicleDto.builder()
                        .vehicleId(vehicle.getVehicleId())
                        .brand(vehicle.getBrandKor())
                        .modelName(vehicle.getModelNameDisplay())
                        .vehicleClass(vehicle.getVehicleClassKor())
                        .priceBasic(null)
                        .pricePremium(null)
                        .drivingRange(vehicle.getDrivingRange())
                        .fastChargingTime(null)
                        .slowChargingTime(null)
                        .chargingMethod(null)
                        .batteryCapacity(null)
                        .build())
                .toList();
    }

    private String getUserId(HttpSession session) {
        Object userId = session.getAttribute("userId");
        if (userId != null) {
            return String.valueOf(userId);
        }

        Object loginUser = session.getAttribute("loginUser");
        if (loginUser instanceof User user) {
            return user.getUserId();
        }

        return null;
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