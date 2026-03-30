package com.evcar.service.vehicle;

import org.springframework.stereotype.Service;
import com.evcar.domain.vehicle.Vehicle;
import com.evcar.dto.vehicle.VehicleDetailDto;
import com.evcar.dto.vehicle.VehicleListDto;
import com.evcar.repository.vehicle.VehicleRepository;

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

            VehicleListDto dto = new VehicleListDto(
                    v.getVehicleId(),
                    v.getBrand(),
                    v.getModelName(),
                    v.getVehicleClass(),
                    v.getPriceBasic(),
                    v.getPricePremium(),
                    v.getDrivingRange(),
                    v.getImageUrl(),
                    v.getCatalogUrl()
            );

            dto.setWished(wishlistService.isWished(v.getVehicleId()));

            return dto;

        }).collect(Collectors.toList());
    }

    // 차량 상세
    @Override
    public VehicleDetailDto getDetail(Long vehicleId) {

        Vehicle v = vehicleRepository.findById(vehicleId)
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

        // 🔥 여기만 수정 (null 방어)
        dto.setBatteryCapacity(
                v.getBatteryCapacity() != null ? v.getBatteryCapacity().doubleValue() : 0.0
        );

        dto.setImageUrl(v.getImageUrl());
        dto.setCatalogUrl(v.getCatalogUrl());

        return dto;
    }

    public Vehicle getVehicleDetail(Long id) {
        return vehicleRepository.findById(id).orElse(null);
    }
}