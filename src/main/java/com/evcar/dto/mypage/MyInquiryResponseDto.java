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
        String replyStatus = inquiry.getReplyStatus();

        return MyInquiryResponseDto.builder()
                .inquiryId(inquiry.getInquiryId())
                .title(inquiry.getTitle())
                .content(inquiry.getContent())
                .replyContent(inquiry.getReplyContent())
                .createdAt(inquiry.getCreatedAt())
                .answered("REPLIED".equals(replyStatus) || "CLOSED".equals(replyStatus))
                .build();
    }

}