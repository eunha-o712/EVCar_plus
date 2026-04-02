package com.evcar.dto.mypage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WithdrawRequestDto {
	
	private String password;
	private String withdrawReason;
	private Boolean agreeWithdraw;
	
	public boolean isInvalid() {
		return password ==null||password.isBlank()
				||withdrawReason==null||withdrawReason.isBlank()
				||agreeWithdraw==null||agreeWithdraw;
	}
}
