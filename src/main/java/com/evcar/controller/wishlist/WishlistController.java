package com.evcar.controller.wishlist;

import com.evcar.domain.user.User;
import com.evcar.service.wishlist.WishlistService;
import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/wishlist")
public class WishlistController {

    private final WishlistService wishlistService;

    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> add(HttpSession session,
                                                   @RequestParam("vehicleId") String vehicleId) {

        String userId = getUserId(session);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(createResponse(false, "로그인이 필요합니다."));
        }

        wishlistService.addWishlist(userId, vehicleId);
        return ResponseEntity.ok(createResponse(true, "관심 차량이 등록되었습니다."));
    }

    @PostMapping("/delete")
    public ResponseEntity<Map<String, Object>> delete(HttpSession session,
                                                      @RequestParam("vehicleId") String vehicleId) {

        String userId = getUserId(session);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(createResponse(false, "로그인이 필요합니다."));
        }

        wishlistService.removeWishlist(userId, vehicleId);
        return ResponseEntity.ok(createResponse(true, "관심 차량이 삭제되었습니다."));
    }

    private String getUserId(HttpSession session) {
        Object userId = session.getAttribute("userId");
        if (userId != null) {
            return String.valueOf(userId);
        }

        Object loginUser = session.getAttribute("loginUser");
        if (loginUser instanceof User user) {
            return user.getUserId();
        }

        return null;
    }

    private Map<String, Object> createResponse(boolean success, String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", success);
        response.put("message", message);
        return response;
    }
}