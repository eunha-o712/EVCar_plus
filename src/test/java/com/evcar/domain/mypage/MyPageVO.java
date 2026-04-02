package com.evcar.domain.mypage;

public class MyPageVO {
	private Long UserId;             //회원고유번호
	private String loginId;          //아이디
	private String name;             //이름
	private String birthDate;        //생년월일
	private String gender;           //성별
	private String phone;            //전화번호
	private String address;          //주소
	private String email;            //이메일
	private String hasVehicle;       //차량소유 여부(yes/no)
	private String vehicleModel;     //보유 차량명
	private Integer vehicleYear;     //차량 연식
	private Integer drivingDistance; //주행거리
	
	public MyPageVO() {
		super();
	}


	public Long getUserId() {
		return UserId;
	}

	public void setUserId(Long userId) {
		UserId = userId;
	}

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
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
