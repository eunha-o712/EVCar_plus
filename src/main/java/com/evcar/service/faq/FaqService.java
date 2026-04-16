package com.evcar.service.faq;

import com.evcar.dto.faq.FaqResponseDto;
import java.util.List;

public interface FaqService {

    List<FaqResponseDto> getFaqList(String keyword);
}