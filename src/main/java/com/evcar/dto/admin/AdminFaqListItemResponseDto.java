package com.evcar.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminFaqListItemResponseDto {

    private Long no;
    private String faqId;
    private String question;
    private LocalDate createdAt;
    private LocalDate updatedAt;
}