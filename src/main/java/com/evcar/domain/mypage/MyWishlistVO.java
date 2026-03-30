package com.evcar.domain.mypage;

public class MyWishlistVO {
	
	private Long wishlistId;
	private Long vehicleId;
	private String brand;
	private String modelName;
	private String priceBasic;
	private String imageUrl;
	
	public MyWishlistVO() {
		super();
	}

	public Long getWishlistId() {
		return wishlistId;
	}

	public void setWishlistId(Long wishlistId) {
		this.wishlistId = wishlistId;
	}

	public Long getVehicleId() {
		return vehicleId;
	}

	public void setVehicleId(Long vehicleId) {
		this.vehicleId = vehicleId;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public String getPriceBasic() {
		return priceBasic;
	}

	public void setPriceBasic(String priceBasic) {
		this.priceBasic = priceBasic;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	
}
