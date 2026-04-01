package com.evcar.dto.admin;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminInquiryPageResponseDto {

    private List<AdminInquiryListItemDto> inquiries;
    private int currentPage;
    private int totalPages;
    private long totalCount;
    private int pageSize;
    private int startPage;
    private int endPage;
    private boolean hasPreviousGroup;
    private boolean hasNextGroup;
    private int previousGroupPage;
    private int nextGroupPage;

    private long allCount;
    private long waitingCount;
    private long completedCount;
}