package com.evcar.service.chatbot;

import com.evcar.domain.consultation.Consultation;
import com.evcar.domain.vehicle.Vehicle;
import com.evcar.dto.chatbot.ConsultationChatbotDto;
import com.evcar.dto.chatbot.FastApiChatRequestDto;
import com.evcar.dto.chatbot.FastApiChatResponseDto;
import com.evcar.dto.chatbot.VehicleChatbotDto;
import com.evcar.repository.consultation.ConsultationRepository;
import com.evcar.repository.vehicle.VehicleRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FastApiChatbotServiceImpl implements FastApiChatbotService {

    private static final String VEHICLE_STATUS_ACTIVE = "ACTIVE";

    private final WebClient fastApiWebClient;
    private final VehicleRepository vehicleRepository;
    private final ConsultationRepository consultationRepository;

    @Override
    public String getReply(String userId, String userMessage) {
        List<VehicleChatbotDto> vehicles = vehicleRepository.findByVehicleStatusOrderByCreatedAtDesc(VEHICLE_STATUS_ACTIVE)
                .stream()
                .map(this::toVehicleChatbotDto)
                .toList();

        List<ConsultationChatbotDto> consultations = consultationRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(this::toConsultationChatbotDto)
                .toList();

        FastApiChatRequestDto requestDto = FastApiChatRequestDto.builder()
                .userMessage(userMessage)
                .vehicles(vehicles)
                .consultations(consultations)
                .build();

        FastApiChatResponseDto responseDto = fastApiWebClient.post()
                .uri("/ai/chat")
                .bodyValue(requestDto)
                .retrieve()
                .bodyToMono(FastApiChatResponseDto.class)
                .block();

        if (responseDto == null || responseDto.getReply() == null) {
            throw new IllegalStateException("챗봇 응답을 가져오지 못했습니다.");
        }

        return responseDto.getReply();
    }

    private VehicleChatbotDto toVehicleChatbotDto(Vehicle vehicle) {
        return VehicleChatbotDto.builder()
                .vehicleId(vehicle.getVehicleId())
                .brand(vehicle.getBrand())
                .modelName(vehicle.getModelName())
                .vehicleClass(vehicle.getVehicleClass())
                .priceBasic(vehicle.getPriceBasic())
                .pricePremium(vehicle.getPricePremium())
                .drivingRange(vehicle.getDrivingRange())
                .fastChargingTime(vehicle.getFastChargingTime())
                .slowChargingTime(vehicle.getSlowChargingTime())
                .chargingMethod(vehicle.getChargingMethod())
                .batteryCapacity(vehicle.getBatteryCapacity() == null ? null : vehicle.getBatteryCapacity().doubleValue())
                .imageUrl(vehicle.getImageUrl())
                .catalogUrl(vehicle.getCatalogUrl())
                .vehicleUrl(vehicle.getVehicleUrl())
                .build();
    }

    private ConsultationChatbotDto toConsultationChatbotDto(Consultation consultation) {
        return ConsultationChatbotDto.builder()
                .consultId(consultation.getConsultId())
                .consultContent(consultation.getConsultContent())
                .consultStatus(consultation.getConsultStatus())
                .consultResult(consultation.getConsultResult())
                .adminReply(consultation.getAdminReply())
                .build();
    }
}