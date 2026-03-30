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
public class AdminInquiryListItemDto {

    private String inquiryId;
    private String userId;
    private String userName;
    private String title;
    private String replyStatus;
    private LocalDate createdAt;
    private boolean hasReply;
}