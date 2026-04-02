package com.evcar.dto.mypage;

import java.time.LocalDateTime;
import com.evcar.domain.inquiry.Inquiry;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyInquiryResponseDto {
	
	private Integer inquiryId;
	private String title;
	private String content;
	private String replyContent;
	private LocalDateTime createdAt;
	private boolean answered;
	
	public static MyInquiryResponseDto from(Inquiry inquiry) {
		return MyInquiryResponseDto.builder()
				.inquiryId(inquiry.getInquiryId())
				.title(inquiry.getTitle())
				.content(inquiry.getContent())
				.replyContent(inquiry.getReplyContent())
				.createdAt(inquiry.getCreatedAt())
				.answered(inquiry.isAnswered())
				.build();
	}
}