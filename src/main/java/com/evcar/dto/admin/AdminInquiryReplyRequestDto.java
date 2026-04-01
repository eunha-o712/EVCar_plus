package com.evcar.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter; // 🔥 추가

@Getter
@Setter // 이 녀석이 없어서 그동안 글자가 안 넘어왔던 겁니다!
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminInquiryReplyRequestDto {
    private String replyContent;
}