package com.evcar.dto.mypage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyWishlistResponseDto {

    private String wishlistId;
    private String brand;
    private String modelName;
    private String vehicleClass;
    private Integer priceBasic;
    private String imageUrl;
    private String detailUrl;
}