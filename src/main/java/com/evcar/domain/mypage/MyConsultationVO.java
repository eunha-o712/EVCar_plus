package com.evcar.domain.mypage;

public class MyConsultationVO {
	
	private String consultId;
	private String preferredDatetime;
	private Integer budget;
	private String purchasePlan;
	private String consultContent;
	private String consultStatus;
	private String consultResult;
	private String adminReply;
	private String createdAt;
	
	public MyConsultationVO() {
		super();
	}

	public String getConsultId() {
		return consultId;
	}

	public void setConsultId(String consultId) {
		this.consultId = consultId;
	}

	public String getPreferredDatetime() {
		return preferredDatetime;
	}

	public void setPreferredDatetime(String preferredDatetime) {
		this.preferredDatetime = preferredDatetime;
	}

	public Integer getBudget() {
		return budget;
	}

	public void setBudget(Integer budget) {
		this.budget = budget;
	}

	public String getPurchasePlan() {
		return purchasePlan;
	}

	public void setPurchasePlan(String purchasePlan) {
		this.purchasePlan = purchasePlan;
	}

	public String getConsultContent() {
		return consultContent;
	}

	public void setConsultContent(String consultContent) {
		this.consultContent = consultContent;
	}

	public String getConsultStatus() {
		return consultStatus;
	}

	public void setConsultStatus(String consultStatus) {
		this.consultStatus = consultStatus;
	}

	public String getConsultResult() {
		return consultResult;
	}

	public void setConsultResult(String consultResult) {
		this.consultResult = consultResult;
	}

	public String getAdminReply() {
		return adminReply;
	}

	public void setAdminReply(String adminReply) {
		this.adminReply = adminReply;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	
	
}
