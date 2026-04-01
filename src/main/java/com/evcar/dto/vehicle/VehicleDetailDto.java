package com.evcar.dto.vehicle;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VehicleDetailDto {

	// 기본 정보
	private String vehicleId;
	private String brand;
	private String modelName;
	private String vehicleClass;

	// 가격
	private Integer priceBasic;
	private Integer pricePremium;

	// 주행 / 충전
	private Integer drivingRange;
	private String fastChargingTime;
	private String slowChargingTime;
	private String chargingMethod;

	// 배터리
	private Double batteryCapacity;

	// 이미지 & 링크
	private String imageUrl;
	private String catalogUrl;

	// 위시리스트 여부
	private boolean wished = false;

	// =========================
	// 🔥 화면 표시용 메서드
	// =========================

	// 브랜드 한글 변환
	public String getBrandKor() {
		if (brand == null)
			return "";
		switch (brand) {
		case "HYUNDAI":
			return "현대";
		case "KIA":
			return "기아";
		default:
			return brand;
		}
	}

	// 차량 클래스 한글 변환
	public String getVehicleClassKor() {
		if (vehicleClass == null)
			return "";
		switch (vehicleClass) {
		case "SMALL_SUV":
			return "소형 SUV";
		case "MID_SUV":
			return "중형 SUV";
		case "LARGE_SUV":
			return "대형 SUV";
		case "SEDAN":
			return "세단";
		default:
			return vehicleClass;
		}
	}

	// 모델명 표시용 (언더바 제거 → KONA ELECTRIC)
	public String getModelNameDisplay() {
		if (modelName == null)
			return "";
		return modelName.replace("_", " ");
	}
	// 충전시간 표시
	public String getFastChargingTimeDisplay() {
	    if (fastChargingTime == null || fastChargingTime.isEmpty()) return "";
	    return "약 " + fastChargingTime + "분";
	}

	// 충전방식 한글 변환
	public String getChargingMethodKor() {
	    if (chargingMethod == null) return "";

	    switch (chargingMethod) {
	        case "DC_COMBO/AC_SLOW":
	            return "급속 / 완속";
	        case "DC_COMBO":
	            return "급속";
	        case "AC_SLOW":
	            return "완속";
	        default:
	            return chargingMethod;
	    }
	}
}