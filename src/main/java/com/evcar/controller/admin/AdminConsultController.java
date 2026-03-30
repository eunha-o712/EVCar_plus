package com.evcar.controller.admin;

import com.evcar.dto.admin.AdminConsultReplyRequestDto;
import com.evcar.service.admin.AdminConsultService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/consultation")
@RequiredArgsConstructor
public class AdminConsultController {

    private final AdminConsultService adminConsultService;

    @GetMapping("")
    public String list(@RequestParam(name = "status", required = false) String status,
                       @RequestParam(name = "keyword", required = false) String keyword,
                       @RequestParam(name = "consultId", required = false) String consultId,
                       @RequestParam(name = "page", defaultValue = "1") int page,
                       Model model) {

        Pageable pageable = PageRequest.of(Math.max(page - 1, 0), 10);
        Page<?> consultPage = adminConsultService.getConsultPage(status, keyword, pageable);

        model.addAttribute("consultPage", consultPage);
        model.addAttribute("consultationList", consultPage.getContent());
        model.addAttribute("selectedStatus", status);
        model.addAttribute("keyword", keyword);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", consultPage.getTotalPages());

        if (consultId != null && !consultId.isBlank()) {
            model.addAttribute("selectedConsultation", adminConsultService.getConsultDetail(consultId));
        }

        return "admin/consultation/list";
    }

    @PostMapping("/{id}/reply")
    public String reply(@PathVariable("id") String id,
                        @ModelAttribute AdminConsultReplyRequestDto dto,
                        @RequestParam(name = "status", required = false) String status,
                        @RequestParam(name = "keyword", required = false) String keyword,
                        @RequestParam(name = "page", defaultValue = "1") int page) {

        adminConsultService.replyConsult(id, dto);

        return "redirect:/admin/consultation?consultId=" + id
                + (status != null && !status.isBlank() ? "&status=" + status : "")
                + (keyword != null && !keyword.isBlank() ? "&keyword=" + keyword : "")
                + "&page=" + page;
    }
}