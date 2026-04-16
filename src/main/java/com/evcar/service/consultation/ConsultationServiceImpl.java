package com.evcar.service.consultation;

import com.evcar.domain.consultation.Consultation;
import com.evcar.domain.user.User;
import com.evcar.domain.vehicle.Vehicle;
import com.evcar.dto.consultation.ConsultationCreateRequestDto;
import com.evcar.dto.consultation.ConsultationResponseDto;
import com.evcar.repository.consultation.ConsultationRepository;
import com.evcar.repository.user.UserRepository;
import com.evcar.repository.vehicle.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ConsultationServiceImpl implements ConsultationService {

    private static final String DEFAULT_CONSULT_STATUS = "PENDING";

    private final ConsultationRepository consultationRepository;
    private final UserRepository userRepository;
    private final VehicleRepository vehicleRepository;

    @Override
    public List<ConsultationResponseDto> getConsultationList(String keyword) {
        List<Consultation> consultationList;

        if (keyword == null || keyword.isBlank()) {
            consultationList = consultationRepository.findAllByOrderByCreatedAtDesc();
        } else {
            consultationList = consultationRepository.findByConsultContentContainingIgnoreCaseOrderByCreatedAtDesc(keyword);
        }

        return consultationList.stream()
                .map(this::toResponseDto)
                .toList();
    }

    @Override
    public ConsultationResponseDto getConsultationDetail(String consultId) {
        Consultation consultation = consultationRepository.findById(consultId)
                .orElseThrow(() -> new IllegalArgumentException("상담 내역을 찾을 수 없습니다."));

        return toResponseDto(consultation);
    }

    @Override
    @Transactional
    public void createConsultation(ConsultationCreateRequestDto requestDto) {
        User user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("회원 PK(userId)를 찾을 수 없습니다: " + requestDto.getUserId()));

        Vehicle vehicle = vehicleRepository.findById(requestDto.getVehicleId())
                .orElseThrow(() -> new IllegalArgumentException("차량 정보를 찾을 수 없습니다: " + requestDto.getVehicleId()));

        Consultation consultation = Consultation.builder()
                .consultId(generateNextConsultId())
                .userId(user.getUserId())
                .vehicleId(vehicle.getVehicleId())
                .preferredDatetime(requestDto.getPreferredDatetime())
                .budget(requestDto.getBudget())
                .purchasePlan(requestDto.getPurchasePlan())
                .consultContent(requestDto.getConsultContent())
                .consultStatus(DEFAULT_CONSULT_STATUS)
                .build();

        consultationRepository.save(consultation);
    }

    private ConsultationResponseDto toResponseDto(Consultation consultation) {
        // userId, vehicleId로 별도 조회
        String userName = userRepository.findById(consultation.getUserId())
                .map(User::getName)
                .orElse("알 수 없음");

        String vehicleModelName = vehicleRepository.findById(consultation.getVehicleId())
                .map(Vehicle::getModelName)
                .orElse("알 수 없음");

        return ConsultationResponseDto.builder()
                .consultId(consultation.getConsultId())
                .userId(consultation.getUserId())
                .userName(userName)
                .vehicleId(consultation.getVehicleId())
                .vehicleModelName(vehicleModelName)
                .preferredDatetime(consultation.getPreferredDatetime())
                .budget(consultation.getBudget())
                .purchasePlan(consultation.getPurchasePlan())
                .consultContent(consultation.getConsultContent())
                .consultStatus(consultation.getConsultStatus())
                .consultResult(consultation.getConsultResult())
                .adminReply(consultation.getAdminReply())
                .createdAt(consultation.getCreatedAt())
                .build();
    }

    private String generateNextConsultId() {
        return consultationRepository.findTopByOrderByConsultIdDesc()
                .map(Consultation::getConsultId)
                .map(lastId -> {
                    int nextNumber = Integer.parseInt(lastId.substring(2)) + 1;
                    return String.format("CS%04d", nextNumber);
                })
                .orElse("CS0001");
    }
}