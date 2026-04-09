package com.evcar.dto.mypage;

import com.evcar.domain.inquiry.Inquiry;
import java.time.LocalDateTime;
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
	private String replyStatus;
	private String replyStatusLabel;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private boolean answered;

	public static MyInquiryResponseDto from(Inquiry inquiry) {
		return MyInquiryResponseDto.builder().inquiryId(inquiry.getInquiryId()).title(inquiry.getTitle())
				.content(inquiry.getContent()).replyContent(inquiry.getReplyContent())
				.replyStatus(inquiry.getReplyStatus()).replyStatusLabel(toReplyStatusLabel(inquiry.getReplyStatus()))
				.createdAt(inquiry.getCreatedAt()).updatedAt(inquiry.getUpdatedAt()).answered(inquiry.isAnswered())
				.build();
	}

	private static String toReplyStatusLabel(String replyStatus) {
		if (replyStatus == null || replyStatus.isBlank()) {
			return "답변 대기";
		}

		return switch (replyStatus) {
		case "WAITING" -> "답변 대기";
		case "REPLIED" -> "답변 완료";
		case "CLOSED" -> "처리 완료";
		default -> replyStatus;
		};
	}
}