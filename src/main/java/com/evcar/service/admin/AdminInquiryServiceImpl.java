package com.evcar.service.admin;

import com.evcar.domain.inquiry.Inquiry;
import com.evcar.dto.admin.AdminInquiryDetailDto;
import com.evcar.dto.admin.AdminInquiryPageResponseDto;
import com.evcar.repository.admin.AdminInquiryQueryRepository;
import com.evcar.repository.inquiry.InquiryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminInquiryServiceImpl implements AdminInquiryService {

    private static final int PAGE_GROUP_SIZE = 10;

    private final AdminInquiryQueryRepository adminInquiryQueryRepository;
    private final InquiryRepository inquiryRepository;

    @Override
    public AdminInquiryPageResponseDto getInquiryPage(int page, int size, String replyStatus, String keyword) {
        int currentPage = Math.max(page, 1);
        int pageSize = size <= 0 ? 10 : size;
        int offset = (currentPage - 1) * pageSize;

        long filteredCount = adminInquiryQueryRepository.countInquiries(replyStatus, keyword);
        int totalPages = (int) Math.ceil((double) filteredCount / pageSize);

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

        long allCount = adminInquiryQueryRepository.countAllInquiries(keyword);
        long waitingCount = adminInquiryQueryRepository.countWaitingInquiries(keyword);
        long completedCount = adminInquiryQueryRepository.countCompletedInquiries(keyword);

        return AdminInquiryPageResponseDto.builder()
                .inquiries(adminInquiryQueryRepository.findInquiryPage(offset, pageSize, replyStatus, keyword))
                .currentPage(currentPage)
                .totalPages(totalPages)
                .totalCount(filteredCount)
                .pageSize(pageSize)
                .startPage(startPage)
                .endPage(endPage)
                .hasPreviousGroup(hasPreviousGroup)
                .hasNextGroup(hasNextGroup)
                .previousGroupPage(hasPreviousGroup ? startPage - 1 : 1)
                .nextGroupPage(hasNextGroup ? endPage + 1 : totalPages)
                .allCount(allCount)
                .waitingCount(waitingCount)
                .completedCount(completedCount)
                .build();
    }

    @Override
    public AdminInquiryDetailDto getInquiryDetail(String inquiryId) {
        return adminInquiryQueryRepository.findInquiryDetail(inquiryId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 문의입니다. ID: " + inquiryId));
    }

    @Override
    @Transactional
    public void saveReply(String inquiryId, String replyContent) {
        String content = replyContent == null ? "" : replyContent.trim();
        String status = StringUtils.hasText(content) ? "REPLIED" : "WAITING";

        Inquiry inquiry = inquiryRepository.findById(inquiryId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 문의입니다. ID: " + inquiryId));

        inquiry.updateReply(content, status);
        inquiryRepository.save(inquiry);
    }
}