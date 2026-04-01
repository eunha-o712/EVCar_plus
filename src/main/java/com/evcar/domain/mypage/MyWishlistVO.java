package com.evcar.domain.mypage;

public class MyWishlistVO {
	
	private String wishlistId;
	private String vehicleId;
	private String brand;
	private String modelName;
	private String priceBasic;
	private String imageUrl;
	
	public MyWishlistVO() {
		super();
	}

	public String getWishlistId() {
		return wishlistId;
	}

	public void setWishlistId(String wishlistId) {
		this.wishlistId = wishlistId;
	}

	public String getVehicleId() {
		return vehicleId;
	}

	public void setVehicleId(String vehicleId) {
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
