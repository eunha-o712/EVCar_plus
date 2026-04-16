package com.evcar.dto.mypage;

public class MyInfoUpdateDto {
	private Long userId; // 회원고유번호
	private String password; // 새 비밀번호(비어있으면 기존 유지)
	private String phone; // 전화번호
	private String address; // 주소
	private String email; // 이메일
	private String hasVehicle; // 차량 소유 여부(yes/no)
	private String vehicleModel; // 보유 차량명(Yes일때만 입력)
	private Integer vehicleYear; // 차량 연식
	private Integer drivingDistance; // 주행거리

	public MyInfoUpdateDto() {
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

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getHasVehicle() {
		return hasVehicle;
	}

	public void setHasVehicle(String hasVehicle) {
		this.hasVehicle = hasVehicle;
	}

	public String getVehicleModel() {
		return vehicleModel;
	}

	public void setVehicleModel(String vehicleModel) {
		this.vehicleModel = vehicleModel;
	}

	public Integer getVehicleYear() {
		return vehicleYear;
	}

	public void setVehicleYear(Integer vehicleYear) {
		this.vehicleYear = vehicleYear;
	}

	public Integer getDrivingDistance() {
		return drivingDistance;
	}

	public void setDrivingDistance(Integer drivingDistance) {
		this.drivingDistance = drivingDistance;
	}

}