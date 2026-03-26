package com.evcar.domain.mypage;

public class UserVO {

	private Long userId;
	private String loginId;
	private String password;
	private String name;
	private String birthDate;
	private String gender;
	private String phone;
	private String address;
	private String email;
	private String hasVehicle;
	private Integer vehicleYear;
	private Integer drivingDistance;
	
	public UserVO() {
		super();
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
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
