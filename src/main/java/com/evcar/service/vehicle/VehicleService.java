package com.evcar.service.vehicle;

import com.evcar.dto.vehicle.VehicleDetailDto;
import com.evcar.dto.vehicle.VehicleListDto;
import java.util.List;

public interface VehicleService {

    List<VehicleListDto> getVehicleList(String brand, String vehicleClass);


    VehicleDetailDto getDetail(String vehicleId);
}
