package com.evcar.controller.faq;

import com.evcar.dto.faq.FaqResponseDto;
import com.evcar.service.faq.FaqService;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/faq")
@RequiredArgsConstructor
public class FaqController {

    private static final int PAGE_SIZE = 10;
    private static final int PAGE_BLOCK_SIZE = 5;

    private final FaqService faqService;

    @GetMapping
    public String faqList(@RequestParam(value = "keyword", required = false) String keyword,
                          @RequestParam(value = "page", defaultValue = "1") int page,
                          Model model) {
        List<FaqResponseDto> faqList = faqService.getFaqList(keyword);
        PaginationResult<FaqResponseDto> paginationResult = paginate(faqList, page);

        model.addAttribute("faqList", paginationResult.items());
        model.addAttribute("keyword", keyword);
        model.addAttribute("currentPage", paginationResult.currentPage());
        model.addAttribute("totalPages", paginationResult.totalPages());
        model.addAttribute("startPage", paginationResult.startPage());
        model.addAttribute("endPage", paginationResult.endPage());
        model.addAttribute("hasPrevious", paginationResult.hasPrevious());
        model.addAttribute("hasNext", paginationResult.hasNext());

        return "faq/list";
    }

    @GetMapping("/search")
    public String faqSearch(@RequestParam(value = "keyword", required = false) String keyword,
                            @RequestParam(value = "page", defaultValue = "1") int page,
                            Model model) {
        List<FaqResponseDto> faqList = faqService.getFaqList(keyword);
        PaginationResult<FaqResponseDto> paginationResult = paginate(faqList, page);

        model.addAttribute("faqList", paginationResult.items());
        model.addAttribute("keyword", keyword);
        model.addAttribute("currentPage", paginationResult.currentPage());
        model.addAttribute("totalPages", paginationResult.totalPages());
        model.addAttribute("startPage", paginationResult.startPage());
        model.addAttribute("endPage", paginationResult.endPage());
        model.addAttribute("hasPrevious", paginationResult.hasPrevious());
        model.addAttribute("hasNext", paginationResult.hasNext());

        return "faq/list";
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

        int currentBlock = (currentPage - 1) / PAGE_BLOCK_SIZE;
        int startPage = currentBlock * PAGE_BLOCK_SIZE + 1;
        int endPage = Math.min(startPage + PAGE_BLOCK_SIZE - 1, totalPages);

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