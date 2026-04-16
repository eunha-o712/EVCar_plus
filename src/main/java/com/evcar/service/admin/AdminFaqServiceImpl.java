package com.evcar.service.admin;

import com.evcar.domain.faq.Faq;
import com.evcar.dto.admin.AdminFaqFormResponseDto;
import com.evcar.dto.admin.AdminFaqListItemResponseDto;
import com.evcar.dto.admin.AdminFaqPageResponseDto;
import com.evcar.dto.admin.AdminFaqSaveRequestDto;
import com.evcar.repository.faq.FaqRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminFaqServiceImpl implements AdminFaqService {

    private static final int PAGE_SIZE = 10;
    private static final int PAGE_GROUP_SIZE = 10;

    private final FaqRepository faqRepository;

    @Override
    public AdminFaqPageResponseDto getFaqPage(int page) {
        int currentPage = Math.max(page, 1);
        long totalCount = faqRepository.count();
        int totalPages = (int) Math.ceil((double) totalCount / PAGE_SIZE);

        if (totalPages > 0 && currentPage > totalPages) {
            currentPage = totalPages;
        }

        int offset = (currentPage - 1) * PAGE_SIZE;
        List<Faq> faqList = totalCount > 0
                ? faqRepository.findFaqPage(PAGE_SIZE, offset)
                : List.of();

        int startPage = ((currentPage - 1) / PAGE_GROUP_SIZE) * PAGE_GROUP_SIZE + 1;
        int endPage = Math.min(startPage + PAGE_GROUP_SIZE - 1, Math.max(totalPages, 1));

        List<AdminFaqListItemResponseDto> listItemDtos = new ArrayList<>();
        for (int i = 0; i < faqList.size(); i++) {
            Faq faq = faqList.get(i);
            long no = totalCount - ((long) (currentPage - 1) * PAGE_SIZE) - i;

            listItemDtos.add(AdminFaqListItemResponseDto.builder()
                    .no(no)
                    .faqId(faq.getFaqId())
                    .question(faq.getQuestion())
                    .createdAt(toLocalDate(faq.getCreatedAt()))
                    .updatedAt(toLocalDate(faq.getUpdatedAt()))
                    .build());
        }

        return AdminFaqPageResponseDto.builder()
                .faqList(listItemDtos)
                .currentPage(currentPage)
                .totalPages(totalPages)
                .totalCount(totalCount)
                .startPage(startPage)
                .endPage(endPage)
                .hasPreviousGroup(startPage > 1)
                .hasNextGroup(endPage < totalPages)
                .build();
    }

    @Override
    public AdminFaqFormResponseDto getFaq(String faqId) {
        Faq faq = faqRepository.findByFaqId(faqId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 FAQ입니다."));

        return AdminFaqFormResponseDto.builder()
                .faqId(faq.getFaqId())
                .question(faq.getQuestion())
                .answer(faq.getAnswer())
                .createdAt(toLocalDate(faq.getCreatedAt()))
                .updatedAt(toLocalDate(faq.getUpdatedAt()))
                .build();
    }

    @Override
    @Transactional
    public void saveFaq(AdminFaqSaveRequestDto requestDto) {
        validateRequest(requestDto);

        if (StringUtils.hasText(requestDto.getFaqId())) {
            updateFaq(requestDto);
            return;
        }

        createFaq(requestDto);
    }

    @Override
    @Transactional
    public void deleteFaq(String faqId) {
        Faq faq = faqRepository.findByFaqId(faqId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 FAQ입니다."));

        faqRepository.delete(faq);
    }

    private void createFaq(AdminFaqSaveRequestDto requestDto) {
        Faq faq = Faq.builder()
                .faqId(generateNextFaqId())
                .question(requestDto.getQuestion().trim())
                .answer(requestDto.getAnswer().trim())
                .build();

        faqRepository.save(faq);
    }

    private void updateFaq(AdminFaqSaveRequestDto requestDto) {
        Faq faq = faqRepository.findByFaqId(requestDto.getFaqId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 FAQ입니다."));

        faq.update(
                requestDto.getQuestion().trim(),
                requestDto.getAnswer().trim()
        );
    }

    private void validateRequest(AdminFaqSaveRequestDto requestDto) {
        if (!StringUtils.hasText(requestDto.getQuestion())) {
            throw new IllegalArgumentException("질문을 입력해주세요.");
        }

        if (!StringUtils.hasText(requestDto.getAnswer())) {
            throw new IllegalArgumentException("답변을 입력해주세요.");
        }
    }

    private String generateNextFaqId() {
        return faqRepository.findLatestFaqId()
                .map(latestId -> {
                    int nextNumber = Integer.parseInt(latestId.substring(3)) + 1;
                    return String.format("faq%04d", nextNumber);
                })
                .orElse("faq0001");
    }

    private LocalDate toLocalDate(java.time.LocalDateTime value) {
        return value == null ? null : value.toLocalDate();
    }
}