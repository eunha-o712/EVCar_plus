package com.evcar.controller.admin;

import com.evcar.dto.admin.AdminInquiryDetailDto;
import com.evcar.dto.admin.AdminInquiryPageResponseDto;
import com.evcar.dto.admin.AdminInquiryReplyRequestDto;
import com.evcar.service.admin.AdminInquiryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/inquiry")
public class AdminInquiryController {

    private final AdminInquiryService adminInquiryService;

    @GetMapping("")
    public String inquiryList(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "replyStatus", required = false) String replyStatus,
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "selectedId", required = false) String selectedId,
            Model model
    ) {
        AdminInquiryPageResponseDto pageResponse = adminInquiryService.getInquiryPage(page, size, replyStatus, keyword);

        String resolvedSelectedId = selectedId;
        if ((resolvedSelectedId == null || resolvedSelectedId.isBlank()) && !pageResponse.getInquiries().isEmpty()) {
            resolvedSelectedId = pageResponse.getInquiries().get(0).getInquiryId();
        }

        AdminInquiryDetailDto detail = null;
        if (resolvedSelectedId != null && !resolvedSelectedId.isBlank()) {
            detail = adminInquiryService.getInquiryDetail(resolvedSelectedId);
        }

        model.addAttribute("pageResponse", pageResponse);
        model.addAttribute("detail", detail);
        model.addAttribute("selectedId", resolvedSelectedId);
        model.addAttribute("replyStatus", replyStatus == null ? "" : replyStatus);
        model.addAttribute("keyword", keyword == null ? "" : keyword);
        model.addAttribute("size", size);

        return "admin/inquiry/list";
    }

    @GetMapping("/{inquiryId}")
    public String inquiryDetailRedirect(
            @PathVariable("inquiryId") String inquiryId,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "replyStatus", required = false) String replyStatus,
            @RequestParam(name = "keyword", required = false) String keyword
    ) {
        StringBuilder redirectUrl = new StringBuilder("redirect:/admin/inquiry?page=")
                .append(page)
                .append("&size=")
                .append(size)
                .append("&selectedId=")
                .append(inquiryId);

        if (replyStatus != null && !replyStatus.isBlank()) {
            redirectUrl.append("&replyStatus=").append(replyStatus);
        }
        if (keyword != null && !keyword.isBlank()) {
            redirectUrl.append("&keyword=").append(keyword);
        }

        return redirectUrl.toString();
    }

    @PostMapping("/{inquiryId}/reply")
    public String saveReply(
            @PathVariable("inquiryId") String inquiryId,
            @ModelAttribute AdminInquiryReplyRequestDto requestDto,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "replyStatus", required = false) String replyStatus,
            @RequestParam(name = "keyword", required = false) String keyword,
            RedirectAttributes redirectAttributes
    ) {
        adminInquiryService.saveReply(inquiryId, requestDto);
        redirectAttributes.addFlashAttribute("message", "문의 답변이 저장되었습니다.");

        StringBuilder redirectUrl = new StringBuilder("redirect:/admin/inquiry?page=")
                .append(page)
                .append("&size=")
                .append(size)
                .append("&selectedId=")
                .append(inquiryId);

        if (replyStatus != null && !replyStatus.isBlank()) {
            redirectUrl.append("&replyStatus=").append(replyStatus);
        }
        if (keyword != null && !keyword.isBlank()) {
            redirectUrl.append("&keyword=").append(keyword);
        }

        return redirectUrl.toString();
    }
}