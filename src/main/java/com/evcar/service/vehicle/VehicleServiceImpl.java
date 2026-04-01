package com.evcar.service.vehicle;

import org.springframework.stereotype.Service;
import com.evcar.domain.vehicle.Vehicle;
import com.evcar.dto.vehicle.VehicleDetailDto;
import com.evcar.dto.vehicle.VehicleListDto;
import com.evcar.repository.vehicle.VehicleRepository;
import com.evcar.service.wishlist.WishlistService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepository;
    private final WishlistService wishlistService;

    public VehicleServiceImpl(VehicleRepository vehicleRepository,
                              WishlistService wishlistService) {
        this.vehicleRepository = vehicleRepository;
        this.wishlistService = wishlistService;
    }

    // 차량 리스트
    @Override
    public List<VehicleListDto> getVehicleList(String brand, String vehicleClass) {

        List<Vehicle> vehicles = vehicleRepository.findByFilter(brand, vehicleClass);

        return vehicles.stream().map(v -> {

            // 🔥 생성자 대신 setter 방식으로 변경
            VehicleListDto dto = new VehicleListDto();

            dto.setVehicleId(v.getVehicleId());
            dto.setBrand(v.getBrand());
            dto.setModelName(v.getModelName());
            dto.setVehicleClass(v.getVehicleClass());
            dto.setPriceBasic(v.getPriceBasic());
            dto.setPricePremium(v.getPricePremium());
            dto.setDrivingRange(v.getDrivingRange());
            dto.setImageUrl(v.getImageUrl());
            dto.setCatalogUrl(v.getCatalogUrl());

            dto.setWished(wishlistService.isWished(v.getVehicleId()));

            return dto;

        }).collect(Collectors.toList());
    }

    // 차량 상세
    @Override
    public VehicleDetailDto getDetail(String id) {

        // 🔥 변수명 오류 수정 (vehicleId → id)
        Vehicle v = vehicleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("차량 없음"));

        VehicleDetailDto dto = new VehicleDetailDto();

        dto.setVehicleId(v.getVehicleId());
        dto.setBrand(v.getBrand());
        dto.setModelName(v.getModelName());
        dto.setVehicleClass(v.getVehicleClass());
        dto.setPriceBasic(v.getPriceBasic());
        dto.setPricePremium(v.getPricePremium());
        dto.setDrivingRange(v.getDrivingRange());
        dto.setFastChargingTime(v.getFastChargingTime());
        dto.setSlowChargingTime(v.getSlowChargingTime());
        dto.setChargingMethod(v.getChargingMethod());

        // 🔥 null 방어
        dto.setBatteryCapacity(
                v.getBatteryCapacity() != null ? v.getBatteryCapacity().doubleValue() : 0.0
        );

        dto.setImageUrl(v.getImageUrl());
        dto.setCatalogUrl(v.getCatalogUrl());

        return dto;
    }

    // 🔥 Long → String으로 수정
    public Vehicle getVehicleDetail(String id) {
        return vehicleRepository.findById(id).orElse(null);
    }
}