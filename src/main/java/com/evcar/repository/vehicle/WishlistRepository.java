package com.evcar.repository.vehicle;

import org.springframework.data.jpa.repository.JpaRepository;
import com.evcar.domain.vehicle.Wishlist;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {

    boolean existsByVehicleId(Long vehicleId);

    void deleteByVehicleId(Long vehicleId);
   

    
}