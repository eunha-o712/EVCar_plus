package com.evcar.service.wishlist;

import com.evcar.domain.wishlist.Wishlist;
import com.evcar.dto.vehicle.VehicleListDto;
import com.evcar.repository.vehicle.VehicleRepository;
import com.evcar.repository.wishlist.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WishlistServiceImpl implements WishlistService {

    private final WishlistRepository wishlistRepository;
    private final VehicleRepository vehicleRepository;

    @Override
    public boolean isWished(String userId, String vehicleId) {
        return wishlistRepository.existsByUserIdAndVehicleId(userId, vehicleId);
    }

    @Override
    public void addWishlist(String userId, String vehicleId) {
        if (wishlistRepository.existsByUserIdAndVehicleId(userId, vehicleId)) {
            return;
        }

        Wishlist wishlist = Wishlist.builder()
                .wishlistId(UUID.randomUUID().toString().substring(0, 20))
                .userId(userId)
                .vehicleId(vehicleId)
                .build(); // createdAt은 @PrePersist가 처리

        wishlistRepository.save(wishlist);
    }

    @Override
    public void removeWishlist(String userId, String vehicleId) {
        wishlistRepository.findByUserIdAndVehicleId(userId, vehicleId)
                .ifPresent(wishlistRepository::delete);
    }

    @Override
    public List<VehicleListDto> getWishlistVehicles(String userId) {
        List<Wishlist> wishlists = wishlistRepository.findByUserIdOrderByCreatedAtDesc(userId);
        List<VehicleListDto> result = new ArrayList<>();

        for (Wishlist wishlist : wishlists) {
            vehicleRepository.findById(wishlist.getVehicleId()).ifPresent(vehicle -> {
                VehicleListDto dto = VehicleListDto.builder()
                        .vehicleId(vehicle.getVehicleId())
                        .brand(vehicle.getBrand())
                        .modelName(vehicle.getModelName())
                        .vehicleClass(vehicle.getVehicleClass())
                        .priceBasic(vehicle.getPriceBasic())
                        .pricePremium(vehicle.getPricePremium())
                        .drivingRange(vehicle.getDrivingRange())
                        .imageUrl(vehicle.getImageUrl())
                        .catalogUrl(vehicle.getCatalogUrl())
                        .wished(true)
                        .build();

                result.add(dto);
            });
        }

        return result;
    }
}