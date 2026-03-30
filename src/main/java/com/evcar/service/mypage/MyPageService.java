package com.evcar.service.mypage;

import com.evcar.dto.mypage.MyConsultationResponseDto;
import com.evcar.dto.mypage.MyInquiryResponseDto;
import com.evcar.dto.mypage.MyPageInfoResponseDto;
import com.evcar.dto.mypage.MyPageInfoUpdateRequestDto;
import com.evcar.dto.mypage.MyPageSummaryResponseDto;
import com.evcar.dto.mypage.MyWishlistResponseDto;
import com.evcar.dto.mypage.WithdrawRequestDto;
import java.util.List;

public interface MyPageService {

    MyPageInfoResponseDto getMyPageInfo(String userId);

    MyPageSummaryResponseDto getMyPageSummary(String userId);

    void updateMyPageInfo(String userId, MyPageInfoUpdateRequestDto requestDto);

    List<MyConsultationResponseDto> getMyConsultations(String userId);

    void cancelMyConsultation(String userId, String consultId);

    List<MyInquiryResponseDto> getMyInquiries(String userId);

    MyInquiryResponseDto getMyInquiryDetail(String userId, String inquiryId);

    void withdraw(String userId, WithdrawRequestDto withdrawRequestDto);

    List<MyWishlistResponseDto> getMyWishlist(String userId);

    void deleteWishlist(String userId, String wishlistId);
}