package com.evcar.service.vehicle;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.evcar.domain.vehicle.Wishlist;
import com.evcar.dto.vehicle.VehicleListDto;
import com.evcar.repository.vehicle.VehicleRepository;
import com.evcar.repository.vehicle.WishlistRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class WishlistServiceImpl implements WishlistService {

    private final WishlistRepository wishlistRepository;
    private final VehicleRepository vehicleRepository;

    @Override
    public boolean isWished(Long vehicleId) {
        return wishlistRepository.existsByVehicleId(vehicleId);
    }

    @Override
    public void add(Long vehicleId) {
        if (!wishlistRepository.existsByVehicleId(vehicleId)) {
            Wishlist w = new Wishlist();
            w.setVehicleId(vehicleId);
            wishlistRepository.save(w);
        }
    }

    @Override
    public void remove(Long vehicleId) {
        if (wishlistRepository.existsByVehicleId(vehicleId)) {
            wishlistRepository.deleteByVehicleId(vehicleId);
        }
    }

    @Override
    public List<VehicleListDto> getWishlistVehicles() {
        List<Wishlist> wishlist = wishlistRepository.findAll();

        return wishlist.stream()
                .map(w -> {
                    // vehicle 정보 조회
                    var vehicle = vehicleRepository.findById(w.getVehicleId()).orElse(null);

                    if (vehicle == null) return null;

                    // DTO 변환 (필드 맞게 수정 필요)
                    VehicleListDto dto = new VehicleListDto();
                    dto.setBrand(vehicle.getBrand());
                    dto.setModelName(vehicle.getModelName()); // 필드명 맞게 수정
                    
                    return dto;
                })
                .filter(v -> v != null)
                .collect(Collectors.toList());
    }
}