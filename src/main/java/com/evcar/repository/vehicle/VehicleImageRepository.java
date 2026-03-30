package com.evcar.repository.vehicle;

import com.evcar.domain.vehicle.VehicleImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VehicleImageRepository extends JpaRepository<VehicleImage, Long> {

    List<VehicleImage> findByVehicleIdOrderByImageOrderAsc(Long vehicleId);
}