package com.evcar.controller.admin;

import com.evcar.dto.admin.AdminInquiryDetailDto;
import com.evcar.dto.admin.AdminInquiryPageResponseDto;
import com.evcar.service.admin.AdminInquiryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/inquiry")
@RequiredArgsConstructor
public class AdminInquiryController {

    private final AdminInquiryService adminInquiryService;

    @GetMapping("")
    public String inquiryList(@RequestParam(name = "page", defaultValue = "1") int page,
                              @RequestParam(name = "size", defaultValue = "10") int size,
                              @RequestParam(name = "replyStatus", required = false) String replyStatus,
                              @RequestParam(name = "keyword", required = false) String keyword,
                              @RequestParam(name = "selectedId", required = false) String selectedId,
                              Model model) {

        AdminInquiryPageResponseDto pageResponse = adminInquiryService.getInquiryPage(page, size, replyStatus, keyword);

        AdminInquiryDetailDto detail = null;
        if (selectedId != null && !selectedId.isEmpty()) {
            detail = adminInquiryService.getInquiryDetail(selectedId);
        }

        model.addAttribute("pageResponse", pageResponse);
        model.addAttribute("detail", detail);
        model.addAttribute("selectedId", selectedId);
        model.addAttribute("page", pageResponse.getCurrentPage());
        model.addAttribute("size", size);
        model.addAttribute("replyStatus", replyStatus == null ? "" : replyStatus);
        model.addAttribute("keyword", keyword == null ? "" : keyword);

        return "admin/inquiry/list";
    }

    @PostMapping("/{inquiryId}/reply")
    public String saveReply(@PathVariable("inquiryId") String inquiryId,
                            @RequestParam(name = "page", defaultValue = "1") int page,
                            @RequestParam(name = "size", defaultValue = "10") int size,
                            @RequestParam(name = "replyStatus", required = false) String replyStatus,
                            @RequestParam(name = "keyword", required = false) String keyword,
                            @RequestParam(name = "replyContent", required = false) String replyContent,
                            RedirectAttributes redirectAttributes) {

        adminInquiryService.saveReply(inquiryId, replyContent);
        redirectAttributes.addFlashAttribute("message", "답변이 정상적으로 저장되었습니다.");

        return "redirect:/admin/inquiry?page=" + page
                + "&size=" + size
                + "&replyStatus=" + (replyStatus == null ? "" : replyStatus)
                + "&keyword=" + (keyword == null ? "" : keyword)
                + "&selectedId=" + inquiryId;
    }
}