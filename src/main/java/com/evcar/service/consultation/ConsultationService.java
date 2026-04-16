package com.evcar.service.consultation;

import com.evcar.dto.consultation.ConsultationCreateRequestDto;
import com.evcar.dto.consultation.ConsultationResponseDto;

import java.util.List;

public interface ConsultationService {

    List<ConsultationResponseDto> getConsultationList(String keyword);

    ConsultationResponseDto getConsultationDetail(String consultId);

    void createConsultation(ConsultationCreateRequestDto requestDto);
}