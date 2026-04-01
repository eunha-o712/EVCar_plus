package com.evcar.repository.mypage;

import com.evcar.dto.mypage.MyWishlistResponseDto;
import java.util.List;

public interface MyWishlistQueryRepository {

    List<MyWishlistResponseDto> findMyWishlistByUserId(String userId);

    void deleteMyWishlistByUserIdAndWishlistId(String userId, String wishlistId);
}