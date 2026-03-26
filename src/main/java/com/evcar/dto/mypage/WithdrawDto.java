package com.evcar.dto.mypage;

public class WithdrawDto {
	
	private Long userId;
	private String password;
	
	public WithdrawDto() {
		super();
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
