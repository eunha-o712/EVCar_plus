package com.evcar.controller.inquiry;

import com.evcar.dto.inquiry.InquiryCreateRequestDto;
import com.evcar.dto.inquiry.InquiryResponseDto;
import com.evcar.service.inquiry.InquiryService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/inquiry")
@RequiredArgsConstructor
public class InquiryController {

    private static final int PAGE_SIZE = 10;

    private final InquiryService inquiryService;

    @GetMapping
    public String inquiryList(@RequestParam(value = "keyword", required = false) String keyword,
                              @RequestParam(value = "page", defaultValue = "1") int page,
                              Model model) {
        List<InquiryResponseDto> inquiryList = inquiryService.getInquiryList(keyword);
        PaginationResult<InquiryResponseDto> paginationResult = paginate(inquiryList, page);

        model.addAttribute("inquiryList", paginationResult.items());
        model.addAttribute("keyword", keyword);
        model.addAttribute("currentPage", paginationResult.currentPage());
        model.addAttribute("totalPages", paginationResult.totalPages());
        model.addAttribute("startPage", paginationResult.startPage());
        model.addAttribute("endPage", paginationResult.endPage());
        model.addAttribute("hasPrevious", paginationResult.hasPrevious());
        model.addAttribute("hasNext", paginationResult.hasNext());

        return "inquiry/list";
    }

    @GetMapping("/form")
    public String inquiryForm(HttpSession session,
                              Model model) {
        String loginId = extractLoginId(session);

        model.addAttribute("inquiryCreateRequestDto", InquiryCreateRequestDto.builder()
                .userId(loginId)
                .build());
        model.addAttribute("loginId", loginId);
        return "inquiry/form";
    }

    @PostMapping("/form")
    public String createInquiry(@Valid @ModelAttribute InquiryCreateRequestDto inquiryCreateRequestDto,
                                BindingResult bindingResult,
                                HttpSession session,
                                Model model) {
        String loginId = extractLoginId(session);

        if (bindingResult.hasErrors()) {
            model.addAttribute("loginId", loginId);
            return "inquiry/form";
        }

        InquiryCreateRequestDto requestDto = InquiryCreateRequestDto.builder()
                .userId(loginId)
                .title(inquiryCreateRequestDto.getTitle())
                .content(inquiryCreateRequestDto.getContent())
                .build();

        inquiryService.createInquiry(requestDto);
        return "redirect:/inquiry";
    }

    @GetMapping("/{id}")
    public String inquiryDetail(@PathVariable("id") String inquiryId,
                                Model model) {
        model.addAttribute("inquiry", inquiryService.getInquiryDetail(inquiryId));
        return "inquiry/detail";
    }

    private String extractLoginId(HttpSession session) {
        Object loginUser = session.getAttribute("loginUser");

        if (loginUser == null) {
            throw new IllegalStateException("로그인 후 이용 가능합니다.");
        }

        try {
            Method getLoginIdMethod = loginUser.getClass().getMethod("getLoginId");
            Object loginId = getLoginIdMethod.invoke(loginUser);

            if (loginId == null || String.valueOf(loginId).isBlank()) {
                throw new IllegalStateException("세션 로그인 아이디가 없습니다.");
            }

            return String.valueOf(loginId);
        } catch (NoSuchMethodException exception) {
            try {
                Method getUserIdMethod = loginUser.getClass().getMethod("getUserId");
                Object userId = getUserIdMethod.invoke(loginUser);

                if (userId == null || String.valueOf(userId).isBlank()) {
                    throw new IllegalStateException("세션 사용자 아이디가 없습니다.");
                }

                return String.valueOf(userId);
            } catch (ReflectiveOperationException innerException) {
                throw new IllegalStateException("loginUser 세션 객체에서 로그인 아이디를 찾을 수 없습니다.", innerException);
            }
        } catch (ReflectiveOperationException exception) {
            throw new IllegalStateException("세션 사용자 정보를 읽는 중 오류가 발생했습니다.", exception);
        }
    }

    private <T> PaginationResult<T> paginate(List<T> source, int page) {
        List<T> safeSource = source == null ? Collections.emptyList() : source;
        int totalCount = safeSource.size();
        int totalPages = Math.max((int) Math.ceil((double) totalCount / PAGE_SIZE), 1);
        int currentPage = Math.min(Math.max(page, 1), totalPages);

        int fromIndex = (currentPage - 1) * PAGE_SIZE;
        int toIndex = Math.min(fromIndex + PAGE_SIZE, totalCount);

        List<T> pageItems = fromIndex >= totalCount
                ? Collections.emptyList()
                : safeSource.subList(fromIndex, toIndex);

        int startPage = Math.max(1, currentPage - 2);
        int endPage = Math.min(totalPages, startPage + 4);
        startPage = Math.max(1, endPage - 4);

        return new PaginationResult<>(
                pageItems,
                currentPage,
                totalPages,
                startPage,
                endPage,
                currentPage > 1,
                currentPage < totalPages
        );
    }

    private record PaginationResult<T>(
            List<T> items,
            int currentPage,
            int totalPages,
            int startPage,
            int endPage,
            boolean hasPrevious,
            boolean hasNext
    ) {
    }
}