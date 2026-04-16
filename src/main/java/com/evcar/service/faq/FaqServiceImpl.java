package com.evcar.service.faq;

import com.evcar.domain.faq.Faq;
import com.evcar.dto.faq.FaqResponseDto;
import com.evcar.repository.faq.FaqRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FaqServiceImpl implements FaqService {

    private final FaqRepository faqRepository;

    @Override
    public List<FaqResponseDto> getFaqList(String keyword) {
        List<Faq> faqList = (keyword == null || keyword.isBlank())
                ? faqRepository.findAllByOrderByCreatedAtDesc()
                : faqRepository.findByQuestionContainingIgnoreCaseOrAnswerContainingIgnoreCaseOrderByCreatedAtDesc(keyword, keyword);

        return faqList.stream()
                .map(this::toResponseDto)
                .toList();
    }

    private FaqResponseDto toResponseDto(Faq faq) {
        return FaqResponseDto.builder()
                .faqId(faq.getFaqId())
                .question(faq.getQuestion())
                .answer(faq.getAnswer())
                .createdAt(faq.getCreatedAt())
                .build();
    }
}