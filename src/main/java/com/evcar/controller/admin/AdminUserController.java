package com.evcar.controller.admin;

import com.evcar.dto.admin.AdminUserDetailResponseDto;
import com.evcar.dto.admin.AdminUserListResponseDto;
import com.evcar.service.admin.AdminUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/user")
public class AdminUserController {

    private final AdminUserService adminUserService;

    @GetMapping("")
    public String userList(
            @RequestParam(name = "status", defaultValue = "ALL") String status,
            @RequestParam(name = "keyword", defaultValue = "") String keyword,
            @RequestParam(name = "selectedUserId", required = false) String selectedUserId,
            Model model
    ) {
        List<AdminUserListResponseDto> userList = adminUserService.getUserList(status, keyword);

        String resolvedSelectedUserId = selectedUserId;
        if (resolvedSelectedUserId == null && !userList.isEmpty()) {
            resolvedSelectedUserId = userList.get(0).getUserId();
        }

        AdminUserDetailResponseDto selectedUser = null;
        if (resolvedSelectedUserId != null) {
            selectedUser = adminUserService.getUserDetail(resolvedSelectedUserId);
        }

        model.addAttribute("userList", userList);
        model.addAttribute("selectedUser", selectedUser);
        model.addAttribute("currentStatus", status);
        model.addAttribute("currentKeyword", keyword);
        model.addAttribute("selectedUserId", resolvedSelectedUserId);

        return "admin/user/list";
    }
}