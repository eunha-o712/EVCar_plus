package com.evcar.repository.wishlist;

import java.util.List;

import com.evcar.domain.mypage.MyWishlistVO;

public interface WishlistRepository {
	int deleteByWishlistId(Long wishlistId);
	
	List<MyWishlistVO> findWishlistByUserId(Long userId);
}
