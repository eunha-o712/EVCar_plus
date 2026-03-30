package com.evcar.service.vehicle;

import java.util.List;
import com.evcar.dto.vehicle.VehicleListDto;

public interface WishlistService {
	boolean isWished(Long vehicleId);
    void add(Long vehicleId);
    void remove(Long vehicleId);
    List<VehicleListDto> getWishlistVehicles();
    
}