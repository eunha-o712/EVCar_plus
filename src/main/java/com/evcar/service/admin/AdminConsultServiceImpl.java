package com.evcar.service.admin;

import com.evcar.domain.consultation.Consultation;
import com.evcar.dto.admin.AdminConsultDetailResponseDto;
import com.evcar.dto.admin.AdminConsultListResponseDto;
import com.evcar.dto.admin.AdminConsultReplyRequestDto;
import com.evcar.repository.admin.AdminConsultQueryRepository;
import com.evcar.repository.consultation.ConsultationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminConsultServiceImpl implements AdminConsultService {

    private final AdminConsultQueryRepository adminConsultQueryRepository;
    private final ConsultationRepository consultationRepository;

    @Override
    public Page<AdminConsultListResponseDto> getConsultPage(String status, String keyword, Pageable pageable) {
        return adminConsultQueryRepository.findConsultPage(status, keyword, pageable);
    }

    @Override
    public AdminConsultDetailResponseDto getConsultDetail(String consultId) {
        return adminConsultQueryRepository.findConsultDetail(consultId)
                .orElseThrow(() -> new IllegalArgumentException("해당 상담 정보를 찾을 수 없습니다."));
    }

    @Override
    @Transactional
    public void replyConsult(String consultId, AdminConsultReplyRequestDto dto) {
        Consultation consultation = consultationRepository.findById(consultId)
                .orElseThrow(() -> new IllegalArgumentException("해당 상담 정보를 찾을 수 없습니다."));

        consultation.updateAdminProcess(
                dto.getConsultStatus(),
                dto.getConsultResult(),
                dto.getAdminReply()
        );
    }
}