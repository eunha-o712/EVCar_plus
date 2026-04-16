package com.evcar.dto.inquiry;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class InquiryResponseDto {

    private String inquiryId;
    private String userId;
    private String userName;
    private String title;
    private String content;
    private String replyContent;
    private String replyStatus;
    private LocalDateTime createdAt;
}