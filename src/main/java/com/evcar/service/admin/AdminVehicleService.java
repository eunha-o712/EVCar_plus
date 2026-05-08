package com.evcar.service.admin;

import com.evcar.common.enums.VehicleBrandType;
import com.evcar.common.enums.VehicleModelType;
import com.evcar.dto.admin.AdminVehicleFormResponseDto;
import com.evcar.dto.admin.AdminVehicleListResponseDto;
import com.evcar.dto.admin.AdminVehicleSaveRequestDto;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface AdminVehicleService {

    List<AdminVehicleListResponseDto> getVehicleList(String status, String keyword);

    AdminVehicleFormResponseDto getVehicleForm(String vehicleId);

    String previewNextVehicleId(String brand);

    void saveVehicle(AdminVehicleSaveRequestDto requestDto, MultipartFile imageFile);

    void deleteVehicle(String vehicleId);

    List<VehicleBrandType> getVehicleBrandTypes();

    List<VehicleModelType> getVehicleModelTypes();
}