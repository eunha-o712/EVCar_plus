<<<<<<< HEAD
package com.evcar.controller.admin;

import com.evcar.dto.admin.AdminFaqPageResponseDto;
import com.evcar.dto.admin.AdminFaqSaveRequestDto;
import com.evcar.service.admin.AdminFaqService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/faq")
public class AdminFaqController {

    private final AdminFaqService adminFaqService;

    @GetMapping
    public String faqList(@RequestParam(name = "page", defaultValue = "1") int page,
                          Model model) {
        AdminFaqPageResponseDto responseDto = adminFaqService.getFaqPage(page);
        model.addAttribute("faqPage", responseDto);
        return "admin/faq/list";
    }

    @GetMapping("/form")
    public String faqCreateForm(Model model) {
        model.addAttribute("faqForm", AdminFaqSaveRequestDto.builder().build());
        model.addAttribute("isEdit", false);
        return "admin/faq/form";
    }

    @GetMapping("/{id}/form")
    public String faqEditForm(@PathVariable("id") String faqId,
                              Model model) {
        model.addAttribute("faqForm", adminFaqService.getFaq(faqId));
        model.addAttribute("isEdit", true);
        return "admin/faq/form";
    }

    @PostMapping("/form")
    public String saveFaq(@ModelAttribute AdminFaqSaveRequestDto requestDto) {
        adminFaqService.saveFaq(requestDto);
        return "redirect:/admin/faq?page=1";
    }

    @PostMapping("/{id}/delete")
    public String deleteFaq(@PathVariable("id") String faqId,
                            @RequestParam(name = "page", defaultValue = "1") int page) {
        adminFaqService.deleteFaq(faqId);
        return "redirect:/admin/faq?page=" + page;
    }
=======
package com.evcar.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/faq")
public class AdminFaqController {

    @GetMapping("")
    public String faqList() {
        return "admin/faq/list";
    }

    @GetMapping("/form")
    public String faqForm() {
        return "admin/faq/form";
    }
>>>>>>> ccef92c2bb418a9ae53ae1d629a62927994a692e
}