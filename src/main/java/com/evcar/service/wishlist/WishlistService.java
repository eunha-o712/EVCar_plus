package com.evcar.service.wishlist;

import java.util.List;
import com.evcar.dto.vehicle.VehicleListDto;

public interface WishlistService {
	boolean isWished(String id);
    void add(String vehicleId);
    void remove(String vehicleId);
    List<VehicleListDto> getWishlistVehicles();
    
}