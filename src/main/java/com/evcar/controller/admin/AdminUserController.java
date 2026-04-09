package com.evcar.controller.admin;

import com.evcar.service.admin.AdminUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin/user")
@RequiredArgsConstructor
public class AdminUserController {

    private final AdminUserService adminUserService;

    @GetMapping("")
    public String list(@RequestParam(name = "status", required = false) String status,
                       @RequestParam(name = "keyword", required = false) String keyword,
                       @RequestParam(name = "userId", required = false) String userId,
                       @RequestParam(name = "page", defaultValue = "1") int page,
                       Model model) {

        Pageable pageable = PageRequest.of(Math.max(page - 1, 0), 10);
        Page<?> userPage = adminUserService.getUserPage(status, keyword, pageable);

        model.addAttribute("userPage", userPage);
        model.addAttribute("userList", userPage.getContent());
        model.addAttribute("selectedStatus", status);
        model.addAttribute("keyword", keyword);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", userPage.getTotalPages());

        if (userId != null && !userId.isBlank()) {
            model.addAttribute("selectedUser", adminUserService.getUserDetail(userId));
        }

        return "admin/user/list";
    }

}