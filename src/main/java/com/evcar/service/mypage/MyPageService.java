package com.evcar.service.mypage;

import com.evcar.dto.mypage.*;
import java.util.List;

public interface MyPageService {

    MyPageInfoResponseDto getMyPageInfo(String userId);

    void updateMyPageInfo(String userId, MyPageInfoUpdateRequestDto requestDto);

    List<MyConsultationResponseDto> getMyConsultations(String userId);

    MyConsultationResponseDto getMyConsultationDetail(String userId, String consultId);

    void cancelMyConsultation(String userId, String consultId);

    List<MyInquiryResponseDto> getMyInquiries(String userId);

    MyInquiryResponseDto getMyInquiryDetail(String userId, String inquiryId);

    List<MyWishlistResponseDto> getMyWishlist(String userId);

    void deleteWishlist(String userId, String wishlistId);

    MyPageSummaryResponseDto getMyPageSummary(String userId);

    void withdraw(String userId, WithdrawRequestDto withdrawRequestDto);
}