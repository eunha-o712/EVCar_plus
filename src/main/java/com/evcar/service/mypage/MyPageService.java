package com.evcar.service.mypage;

import com.evcar.dto.mypage.MyConsultationResponseDto;
import com.evcar.dto.mypage.MyInquiryResponseDto;
import com.evcar.dto.mypage.MyPageInfoResponseDto;
import com.evcar.dto.mypage.MyPageInfoUpdateRequestDto;
import com.evcar.dto.mypage.WithdrawRequestDto;
import java.util.List;

public interface MyPageService {

    MyPageInfoResponseDto getMyPageInfo(Integer userId);

    void updateMyPageInfo(Integer userId, MyPageInfoUpdateRequestDto requestDto);

    List<MyConsultationResponseDto> getMyConsultations(Integer userId);

    void cancelMyConsultation(Integer userId, Integer consultId);

    List<MyInquiryResponseDto> getMyInquiries(Integer userId);

    
    void withdraw(Integer userId, WithdrawRequestDto requestDto);
}


