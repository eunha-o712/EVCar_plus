package com.evcar.repository.vehicle;

import com.evcar.domain.vehicle.VehicleImage;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleImageRepository extends JpaRepository<VehicleImage, String> {

    List<VehicleImage> findByVehicleIdOrderByImageOrderAsc(String vehicleId);
}