package com.evcar.dto.faq;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class FaqResponseDto {

    private String faqId;
    private String question;
    private String answer;
    private LocalDateTime createdAt;
}