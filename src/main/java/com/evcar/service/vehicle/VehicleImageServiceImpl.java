package com.evcar.service.vehicle;

import com.evcar.domain.vehicle.VehicleImage;
import com.evcar.dto.vehicle.VehicleImageResponseDto;
import com.evcar.repository.vehicle.VehicleImageRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VehicleImageServiceImpl implements VehicleImageService {

    private final VehicleImageRepository vehicleImageRepository;

    @Override
    public List<VehicleImageResponseDto> getImages(String vehicleId) {
        List<VehicleImage> images = vehicleImageRepository.findByVehicleIdOrderByImageOrderAsc(vehicleId);

        return images.stream()
                .map(image -> VehicleImageResponseDto.builder()
                        .imageId(image.getImageId())
                        .vehicleId(image.getVehicleId())
                        .imageUrl(image.getImageUrl())
                        .imageOrder(image.getImageOrder())
                        .build())

                .toList();
    }
}