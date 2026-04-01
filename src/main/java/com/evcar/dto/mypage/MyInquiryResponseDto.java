package com.evcar.dto.mypage;

import com.evcar.domain.inquiry.Inquiry;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyInquiryResponseDto {

    private String inquiryId;
    private String title;
    private String content;
    private String replyContent;
    private LocalDate createdAt;
    private boolean answered;

    public static MyInquiryResponseDto from(Inquiry inquiry) {
        return MyInquiryResponseDto.builder()
                .inquiryId(inquiry.getInquiryId())
                .title(inquiry.getTitle())
                .content(inquiry.getContent())
                .replyContent(inquiry.getReplyContent())
                .createdAt(inquiry.getCreatedAt())
                // [수정된 부분] replyStatus 값을 확인하여 boolean으로 변환
                .answered("COMPLETED".equals(inquiry.getReplyStatus()))
                .build();
    }
}