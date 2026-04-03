<<<<<<< HEAD
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
=======
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
>>>>>>> ccef92c2bb418a9ae53ae1d629a62927994a692e
}