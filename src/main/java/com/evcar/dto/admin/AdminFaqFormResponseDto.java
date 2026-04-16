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
public class AdminFaqFormResponseDto {

    private String faqId;
    private String question;
    private String answer;
    private LocalDate createdAt;
    private LocalDate updatedAt;
}