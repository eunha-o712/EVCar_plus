package com.evcar.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminFaqPageResponseDto {

    private List<AdminFaqListItemResponseDto> faqList;
    private int currentPage;
    private int totalPages;
    private long totalCount;
    private int startPage;
    private int endPage;
    private boolean hasPreviousGroup;
    private boolean hasNextGroup;
}