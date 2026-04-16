package com.evcar.dto.admin;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminInquiryDetailDto {

    private String inquiryId;
    private String userId;
    private String userName;
    private String phone;
    private String email;
    private String title;
    private String content;
    private String replyContent;
    private String replyStatus;
    private LocalDate createdAt;
    private LocalDate updatedAt;

    public String getReplyStatusDesc() {
        if ("WAITING".equals(this.replyStatus)) {
            return "대기";
        }
        return "완료";
    }

    public boolean isCompleted() {
        return "REPLIED".equals(this.replyStatus) || "CLOSED".equals(this.replyStatus);
    }
}