package com.evcar.service.mypage;

import com.evcar.dto.mypage.MyConsultationResponseDto;
import com.evcar.dto.mypage.MyInquiryResponseDto;
import com.evcar.dto.mypage.MyPageInfoResponseDto;
import com.evcar.dto.mypage.MyPageInfoUpdateRequestDto;
import com.evcar.dto.mypage.WithdrawRequestDto;
import java.util.List;

public interface MyPageService {

    MyPageInfoResponseDto getMyPageInfo(String loginId);

    void updateMyPageInfo(String loginId, MyPageInfoUpdateRequestDto requestDto);

    List<MyConsultationResponseDto> getMyConsultations(String loginId);

    void cancelMyConsultation(String loginId, Integer consultId);

    List<MyInquiryResponseDto> getMyInquiries(String loginId);

    void withdraw(String loginId, WithdrawRequestDto withdrawRequestDto);
}

