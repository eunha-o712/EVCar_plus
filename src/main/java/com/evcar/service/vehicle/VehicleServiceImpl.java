package com.evcar.service.vehicle;

import com.evcar.domain.vehicle.Vehicle;
import com.evcar.dto.vehicle.VehicleDetailDto;
import com.evcar.dto.vehicle.VehicleListDto;
import com.evcar.repository.vehicle.VehicleRepository;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VehicleServiceImpl implements VehicleService {

    private static final String VEHICLE_STATUS_ACTIVE = "ACTIVE";

    private final VehicleRepository vehicleRepository;

    @Override
    public List<VehicleListDto> getVehicleList(String brand, String vehicleClass) {
        List<Vehicle> vehicles;

        boolean hasBrand = StringUtils.hasText(brand);
        boolean hasVehicleClass = StringUtils.hasText(vehicleClass);

        if (hasBrand && hasVehicleClass) {
            vehicles = vehicleRepository.findByVehicleStatusAndBrandAndVehicleClassOrderByCreatedAtDesc(
                    VEHICLE_STATUS_ACTIVE, brand, vehicleClass);
        } else if (hasBrand) {
            vehicles = vehicleRepository.findByVehicleStatusAndBrandOrderByCreatedAtDesc(
                    VEHICLE_STATUS_ACTIVE, brand);
        } else if (hasVehicleClass) {
            vehicles = vehicleRepository.findByVehicleStatusAndVehicleClassOrderByCreatedAtDesc(
                    VEHICLE_STATUS_ACTIVE, vehicleClass);
        } else {
            vehicles = vehicleRepository.findByVehicleStatusOrderByCreatedAtDesc(VEHICLE_STATUS_ACTIVE);
        }

        return vehicles.stream()
                .map(v -> VehicleListDto.builder()
                        .vehicleId(v.getVehicleId())
                        .brand(v.getBrand())
                        .modelName(v.getModelName())
                        .vehicleClass(v.getVehicleClass())
                        .priceBasic(v.getPriceBasic())
                        .pricePremium(v.getPricePremium())
                        .drivingRange(v.getDrivingRange())
                        .imageUrl(v.getImageUrl())
                        .catalogUrl(v.getCatalogUrl())
                        .build())
                .toList();
    }

    @Override
    public VehicleDetailDto getDetail(String vehicleId) {
        Vehicle v = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new IllegalArgumentException("해당 차량 정보를 찾을 수 없습니다."));

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
        dto.setBatteryCapacity(
                v.getBatteryCapacity() != null ? v.getBatteryCapacity().doubleValue() : 0.0);
        dto.setImageUrl(v.getImageUrl());
        dto.setCatalogUrl(v.getCatalogUrl());

        return dto;
    }
}