package com.evcar.service.admin;

import com.evcar.domain.vehicle.Vehicle;
import com.evcar.dto.admin.AdminVehicleFormResponseDto;
import com.evcar.dto.admin.AdminVehicleListResponseDto;
import com.evcar.dto.admin.AdminVehicleSaveRequestDto;
import com.evcar.repository.vehicle.VehicleRepository;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminVehicleServiceImpl implements AdminVehicleService {

    private static final String VEHICLE_STATUS_ACTIVE = "ACTIVE";
    private static final String VEHICLE_STATUS_SOLD_OUT = "SOLD_OUT";
    private static final String UPLOAD_DIRECTORY = "uploads/images";
    private static final String UPLOAD_URL_PREFIX = "/images/upload/";

    private final VehicleRepository vehicleRepository;

    @Override
    public List<AdminVehicleListResponseDto> getVehicleList(String status, String keyword) {
        String normalizedStatus = normalizeStatus(status);
        String rawKeyword = normalizeKeyword(keyword);
        String normalizedKeyword = normalizeModelKeyword(rawKeyword);

        return vehicleRepository.searchAdminVehicleList(normalizedStatus, rawKeyword, normalizedKeyword).stream()
                .map(vehicle -> AdminVehicleListResponseDto.builder()
                        .vehicleId(vehicle.getVehicleId())
                        .brand(vehicle.getBrand())
                        .modelName(vehicle.getModelName())
                        .vehicleClass(vehicle.getVehicleClass())
                        .vehicleStatus(vehicle.getVehicleStatus())
                        .priceBasic(vehicle.getPriceBasic())
                        .imageUrl(vehicle.getImageUrl())
                        .createdAt(vehicle.getCreatedAt())
                        .updatedAt(vehicle.getUpdatedAt())
                        .build())
                .toList();
    }
    @Override
    public AdminVehicleFormResponseDto getVehicleForm(String vehicleId) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new IllegalArgumentException("해당 차량 정보를 찾을 수 없습니다."));

        return AdminVehicleFormResponseDto.builder()
                .vehicleId(vehicle.getVehicleId())
                .brand(vehicle.getBrand())
                .modelName(vehicle.getModelName())
                .vehicleClass(vehicle.getVehicleClass())
                .vehicleStatus(vehicle.getVehicleStatus())
                .priceBasic(vehicle.getPriceBasic())
                .pricePremium(vehicle.getPricePremium())
                .drivingRange(vehicle.getDrivingRange())
                .fastChargingTime(vehicle.getFastChargingTime())
                .slowChargingTime(vehicle.getSlowChargingTime())
                .chargingMethod(vehicle.getChargingMethod())
                .batteryCapacity(vehicle.getBatteryCapacity())
                .imageUrl(vehicle.getImageUrl())
                .catalogUrl(vehicle.getCatalogUrl())
                .vehicleUrl(vehicle.getVehicleUrl())
                .createdAt(vehicle.getCreatedAt())
                .updatedAt(vehicle.getUpdatedAt())
                .build();
    }
  
    @Override
    public String previewNextVehicleId(String brand) {
        return generateNextVehicleId(brand);
    }

    @Override
    @Transactional
    public void saveVehicle(AdminVehicleSaveRequestDto requestDto, MultipartFile imageFile) {
        validateRequest(requestDto);

        if (StringUtils.hasText(requestDto.getVehicleId())) {
            updateVehicle(requestDto, imageFile);
            return;
        }

        createVehicle(requestDto, imageFile);
    }

    @Override
    @Transactional
    public void deleteVehicle(String vehicleId) {
        Vehicle savedVehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new IllegalArgumentException("삭제할 차량 정보를 찾을 수 없습니다."));

        String imageUrl = savedVehicle.getImageUrl();
        vehicleRepository.delete(savedVehicle);
        deleteUploadFileIfExists(imageUrl);
    }

    private void createVehicle(AdminVehicleSaveRequestDto requestDto, MultipartFile imageFile) {
        LocalDateTime now = LocalDateTime.now();
        String storedImageUrl = resolveImageUrl(defaultString(requestDto.getImageUrl()), imageFile);

        Vehicle vehicle = Vehicle.builder()
        		.vehicleId(generateNextVehicleId(requestDto.getBrand()))
                .brand(requestDto.getBrand().trim())
                .modelName(requestDto.getModelName().trim())
                .vehicleClass(requestDto.getVehicleClass().trim())
                .vehicleStatus(normalizeSaveStatus(requestDto.getVehicleStatus()))
                .priceBasic(requestDto.getPriceBasic())
                .pricePremium(defaultInteger(requestDto.getPricePremium()))
                .drivingRange(defaultInteger(requestDto.getDrivingRange()))
                .fastChargingTime(defaultString(requestDto.getFastChargingTime()))
                .slowChargingTime(defaultString(requestDto.getSlowChargingTime()))
                .chargingMethod(defaultString(requestDto.getChargingMethod()))
                .batteryCapacity(defaultBigDecimal(requestDto.getBatteryCapacity()))
                .imageUrl(storedImageUrl)
                .catalogUrl(defaultString(requestDto.getCatalogUrl()))
                .vehicleUrl(defaultString(requestDto.getVehicleUrl()))
                .createdAt(now)
                .updatedAt(now)
                .build();

        vehicleRepository.save(vehicle);
    }

    private void updateVehicle(AdminVehicleSaveRequestDto requestDto, MultipartFile imageFile) {
        Vehicle savedVehicle = vehicleRepository.findById(requestDto.getVehicleId())
                .orElseThrow(() -> new IllegalArgumentException("수정할 차량 정보를 찾을 수 없습니다."));

        String oldImageUrl = savedVehicle.getImageUrl();
        String storedImageUrl = resolveImageUrl(defaultString(requestDto.getImageUrl()), imageFile);

        Vehicle updatedVehicle = Vehicle.builder()
                .vehicleId(savedVehicle.getVehicleId())
                .brand(requestDto.getBrand().trim())
                .modelName(requestDto.getModelName().trim())
                .vehicleClass(requestDto.getVehicleClass().trim())
                .vehicleStatus(normalizeSaveStatus(requestDto.getVehicleStatus()))
                .priceBasic(requestDto.getPriceBasic())
                .pricePremium(defaultInteger(requestDto.getPricePremium()))
                .drivingRange(defaultInteger(requestDto.getDrivingRange()))
                .fastChargingTime(defaultString(requestDto.getFastChargingTime()))
                .slowChargingTime(defaultString(requestDto.getSlowChargingTime()))
                .chargingMethod(defaultString(requestDto.getChargingMethod()))
                .batteryCapacity(defaultBigDecimal(requestDto.getBatteryCapacity()))
                .imageUrl(storedImageUrl)
                .catalogUrl(defaultString(requestDto.getCatalogUrl()))
                .vehicleUrl(defaultString(requestDto.getVehicleUrl()))
                .createdAt(savedVehicle.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .build();

        vehicleRepository.save(updatedVehicle);

        boolean hasNewUpload = imageFile != null && !imageFile.isEmpty();
        if (hasNewUpload && !storedImageUrl.equals(oldImageUrl)) {
            deleteUploadFileIfExists(oldImageUrl);
        }
    }

    private void validateRequest(AdminVehicleSaveRequestDto requestDto) {
        if (!StringUtils.hasText(requestDto.getBrand())) {
            throw new IllegalArgumentException("브랜드명을 입력해 주세요.");
        }
        if (!StringUtils.hasText(requestDto.getModelName())) {
            throw new IllegalArgumentException("차량 모델명을 입력해 주세요.");
        }
        if (!StringUtils.hasText(requestDto.getVehicleClass())) {
            throw new IllegalArgumentException("차급을 입력해 주세요.");
        }
        if (requestDto.getPriceBasic() == null || requestDto.getPriceBasic() < 0) {
            throw new IllegalArgumentException("기본가는 0 이상으로 입력해 주세요.");
        }
        if (requestDto.getPricePremium() != null && requestDto.getPricePremium() < 0) {
            throw new IllegalArgumentException("프리미엄가는 0 이상으로 입력해 주세요.");
        }
        if (requestDto.getDrivingRange() != null && requestDto.getDrivingRange() < 0) {
            throw new IllegalArgumentException("주행거리는 0 이상으로 입력해 주세요.");
        }
        if (requestDto.getBatteryCapacity() != null && requestDto.getBatteryCapacity().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("배터리 용량은 0 이상으로 입력해 주세요.");
        }
    }

    private String resolveImageUrl(String currentImageUrl, MultipartFile imageFile) {
        if (imageFile != null && !imageFile.isEmpty()) {
            return saveImageFile(imageFile);
        }
        return currentImageUrl;
    }

    private String saveImageFile(MultipartFile imageFile) {
        validateImageFile(imageFile);

        String originalFilename = imageFile.getOriginalFilename();
        String extension = extractExtension(originalFilename);
        String savedFileName = UUID.randomUUID().toString().replace("-", "") + extension;

        Path uploadDirectoryPath = Paths.get(System.getProperty("user.dir"), UPLOAD_DIRECTORY);
        Path savePath = uploadDirectoryPath.resolve(savedFileName);

        try {
            Files.createDirectories(uploadDirectoryPath);
            Files.copy(imageFile.getInputStream(), savePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException exception) {
            throw new IllegalStateException("대표 이미지 업로드 중 오류가 발생했습니다.", exception);
        }

        return UPLOAD_URL_PREFIX + savedFileName;
    }

    private void validateImageFile(MultipartFile imageFile) {
        String contentType = imageFile.getContentType();
        if (!StringUtils.hasText(contentType) || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("이미지 파일만 업로드할 수 있습니다.");
        }
    }

    private String extractExtension(String filename) {
        if (!StringUtils.hasText(filename) || !filename.contains(".")) {
            return ".png";
        }
        return filename.substring(filename.lastIndexOf("."));
    }

    private void deleteUploadFileIfExists(String imageUrl) {
        if (!StringUtils.hasText(imageUrl) || !imageUrl.startsWith(UPLOAD_URL_PREFIX)) {
            return;
        }

        String fileName = imageUrl.substring(UPLOAD_URL_PREFIX.length());
        Path filePath = Paths.get(System.getProperty("user.dir"), UPLOAD_DIRECTORY, fileName);

        try {
            Files.deleteIfExists(filePath);
        } catch (IOException exception) {
            throw new IllegalStateException("업로드 이미지 삭제 중 오류가 발생했습니다.", exception);
        }
    }

    private String normalizeStatus(String status) {
        if (!StringUtils.hasText(status)) {
            return null;
        }
        if (VEHICLE_STATUS_SOLD_OUT.equalsIgnoreCase(status)) {
            return VEHICLE_STATUS_SOLD_OUT;
        }
        if (VEHICLE_STATUS_ACTIVE.equalsIgnoreCase(status)) {
            return VEHICLE_STATUS_ACTIVE;
        }
        return null;
    }

    private String normalizeSaveStatus(String status) {
        if (VEHICLE_STATUS_SOLD_OUT.equalsIgnoreCase(status)) {
            return VEHICLE_STATUS_SOLD_OUT;
        }
        return VEHICLE_STATUS_ACTIVE;
    }

    private String normalizeKeyword(String keyword) {
        if (!StringUtils.hasText(keyword)) {
            return null;
        }
        return keyword.trim();
    }

    private Integer defaultInteger(Integer value) {
        return value == null ? 0 : value;
    }

    private BigDecimal defaultBigDecimal(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }

    private String defaultString(String value) {
        return StringUtils.hasText(value) ? value.trim() : "";
    }

    private String generateNextVehicleId(String brand) {
        String prefix = createVehiclePrefix(brand);
        String lastVehicleId = vehicleRepository.findTopVehicleIdByPrefix(prefix).orElse(null);

        if (!StringUtils.hasText(lastVehicleId)) {
            return prefix + "0001";
        }

        String numberPart = lastVehicleId.substring(prefix.length());
        int nextNumber = Integer.parseInt(numberPart) + 1;
        return prefix + String.format("%04d", nextNumber);
    }

    private String createVehiclePrefix(String brand) {
        String normalizedBrand = defaultString(brand).toUpperCase();

        if ("HYUNDAI".equals(normalizedBrand) || "현대".equals(brand)) {
            return "HD";
        }
        if ("KIA".equals(normalizedBrand) || "기아".equals(brand)) {
            return "KA";
        }

        String onlyLetters = normalizedBrand.replaceAll("[^A-Z]", "");
        if (onlyLetters.length() >= 2) {
            return onlyLetters.substring(0, 2);
        }
        if (onlyLetters.length() == 1) {
            return onlyLetters + "X";
        }
        return "VH";
        
        
    }
    private String normalizeModelKeyword(String keyword) {
        if (!StringUtils.hasText(keyword)) {
            return null;
        }

        String normalized = keyword.trim()
                .replace(" ", "")
                .replace("-", "")
                .replace("_", "")
                .toUpperCase();

        return switch (normalized) {
            case "아이오닉5", "IONIQ5" -> "IONIQ_5";
            case "아이오닉5N", "IONIQ5N" -> "IONIQ_5_N";
            case "아이오닉6", "IONIQ6" -> "IONIQ_6";
            case "아이오닉9", "IONIQ9" -> "IONIQ_9";
            case "캐스퍼", "캐스퍼EV", "캐스퍼전기차", "CASPER", "CASPEREV" -> "CASPER_EV";
            case "EV3" -> "EV3";
            case "EV6" -> "EV6";
            case "EV9" -> "EV9";
            default -> keyword;
        };
    }
    private String normalizeBrandKeyword(String keyword) {
        if (!StringUtils.hasText(keyword)) {
            return null;
        }

        String normalized = keyword.trim().replace(" ", "").toUpperCase();

        return switch (normalized) {
            case "현대", "HYUNDAI" -> "HYUNDAI";
            case "기아", "KIA" -> "KIA";
            default -> keyword;
        };
    }
}