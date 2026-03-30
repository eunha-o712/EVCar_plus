package com.evcar.service.admin;

import com.evcar.dto.admin.AdminInquiryDetailDto;
import com.evcar.dto.admin.AdminInquiryPageResponseDto;
import com.evcar.dto.admin.AdminInquiryReplyRequestDto;
import com.evcar.repository.admin.AdminInquiryQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminInquiryServiceImpl implements AdminInquiryService {

    private static final int PAGE_GROUP_SIZE = 10;

    private final AdminInquiryQueryRepository adminInquiryRepository;

    @Override
    public AdminInquiryPageResponseDto getInquiryPage(int page, int size, String replyStatus, String keyword) {
        int currentPage = Math.max(page, 1);
        int pageSize = size <= 0 ? 10 : size;
        int offset = (currentPage - 1) * pageSize;

        long totalCount = adminInquiryRepository.countInquiries(replyStatus, keyword);
        int totalPages = (int) Math.ceil((double) totalCount / pageSize);

        if (totalPages == 0) {
            totalPages = 1;
        }

        if (currentPage > totalPages) {
            currentPage = totalPages;
            offset = (currentPage - 1) * pageSize;
        }

        int startPage = ((currentPage - 1) / PAGE_GROUP_SIZE) * PAGE_GROUP_SIZE + 1;
        int endPage = Math.min(startPage + PAGE_GROUP_SIZE - 1, totalPages);
        boolean hasPreviousGroup = startPage > 1;
        boolean hasNextGroup = endPage < totalPages;

        return AdminInquiryPageResponseDto.builder()
                .inquiries(adminInquiryRepository.findInquiryPage(offset, pageSize, replyStatus, keyword))
                .currentPage(currentPage)
                .totalPages(totalPages)
                .totalCount(totalCount)
                .pageSize(pageSize)
                .startPage(startPage)
                .endPage(endPage)
                .hasPreviousGroup(hasPreviousGroup)
                .hasNextGroup(hasNextGroup)
                .previousGroupPage(hasPreviousGroup ? startPage - 1 : 1)
                .nextGroupPage(hasNextGroup ? endPage + 1 : totalPages)
                .build();
    }

    @Override
    public AdminInquiryDetailDto getInquiryDetail(String inquiryId) {
        return adminInquiryRepository.findInquiryDetail(inquiryId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 문의입니다."));
    }

    @Override
    @Transactional
    public void saveReply(String inquiryId, AdminInquiryReplyRequestDto requestDto) {
        String replyContent = requestDto.getReplyContent() == null ? "" : requestDto.getReplyContent().trim();
        String replyStatus = StringUtils.hasText(replyContent) ? "COMPLETED" : "WAITING";

        adminInquiryRepository.updateReply(inquiryId, replyContent, replyStatus);
    }
}