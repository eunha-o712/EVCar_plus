package com.evcar.service.inquiry;

import com.evcar.domain.inquiry.Inquiry;
import com.evcar.domain.user.User;
import com.evcar.dto.inquiry.InquiryCreateRequestDto;
import com.evcar.dto.inquiry.InquiryResponseDto;
import com.evcar.repository.inquiry.InquiryRepository;
import com.evcar.repository.user.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InquiryServiceImpl implements InquiryService {

    private static final String DEFAULT_REPLY_STATUS = "WAITING";

    private final InquiryRepository inquiryRepository;
    private final UserRepository userRepository;

    @Override
    public List<InquiryResponseDto> getInquiryList(String keyword) {
        List<Inquiry> inquiryList = (keyword == null || keyword.isBlank())
                ? inquiryRepository.findAllByOrderByCreatedAtDesc()
                : inquiryRepository.findByTitleContainingIgnoreCaseOrderByCreatedAtDesc(keyword);

        return inquiryList.stream()
                .map(this::toResponseDto)
                .toList();
    }

    @Override
    public InquiryResponseDto getInquiryDetail(String inquiryId) {
        Inquiry inquiry = inquiryRepository.findById(inquiryId)
                .orElseThrow(() -> new IllegalArgumentException("문의글을 찾을 수 없습니다."));

        return toResponseDto(inquiry);
    }

    @Override
    @Transactional
    public void createInquiry(InquiryCreateRequestDto requestDto) {
        User user = userRepository.findByUserId(requestDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다."));

        Inquiry inquiry = Inquiry.builder()
                .inquiryId(generateNextInquiryId())
                .userId(user.getUserId())
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .replyStatus(DEFAULT_REPLY_STATUS)
                .build();

        inquiryRepository.save(inquiry);
    }

    private InquiryResponseDto toResponseDto(Inquiry inquiry) {
        String userName = userRepository.findByUserId(inquiry.getUserId())
                .map(User::getName)
                .orElse("알 수 없음");

        return InquiryResponseDto.builder()
                .inquiryId(inquiry.getInquiryId())
                .userId(inquiry.getUserId())
                .userName(userName)
                .title(inquiry.getTitle())
                .content(inquiry.getContent())
                .replyContent(inquiry.getReplyContent())
                .replyStatus(inquiry.getReplyStatus())
                .createdAt(inquiry.getCreatedAt())
                .build();
    }

    private String generateNextInquiryId() {
        return inquiryRepository.findTopByOrderByInquiryIdDesc()
                .map(Inquiry::getInquiryId)
                .map(this::incrementInquiryId)
                .orElse("IQ0001");
    }

    private String incrementInquiryId(String lastId) {
        int nextNumber = Integer.parseInt(lastId.substring(2)) + 1;
        return String.format("IQ%04d", nextNumber);
    }
}