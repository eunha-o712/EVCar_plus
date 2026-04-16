package com.evcar.dto.mypage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyPageSummaryResponseDto {

	private int wishlistCount;
	private int consultationCount;
	private int inquiryCount;
	private int waitingInquiryCount;
}