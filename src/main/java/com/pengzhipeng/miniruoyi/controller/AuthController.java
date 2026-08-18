package com.pengzhipeng.miniruoyi.controller;

import com.pengzhipeng.miniruoyi.common.ApiResponse;
import com.pengzhipeng.miniruoyi.domain.User;
import com.pengzhipeng.miniruoyi.dto.ProfileResponse;
import com.pengzhipeng.miniruoyi.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AuthController {

    private static final String LOGIN_USER = "LOGIN_USER";

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<ProfileResponse>> login(
            @RequestParam String username,
            @RequestParam String password,
            HttpSession session) {
        User user = userService.authenticate(username, password);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(401, "用户名或密码错误"));
        }

        session.setAttribute(LOGIN_USER, user);
        return ResponseEntity.ok(
                ApiResponse.success("登录成功", new ProfileResponse(user))
        );
    }

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<ProfileResponse>> profile(HttpSession session) {
        User user = currentUser(session);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(401, "未登录或登录已过期"));
        }
        if (!"dashboard:view".equals(user.getPermission())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error(403, "没有访问这个页面的权限"));
        }

        return ResponseEntity.ok(ApiResponse.success("已登录", new ProfileResponse(user)));
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout(HttpSession session) {
        session.invalidate();
        return ApiResponse.success("已退出登录", null);
    }

    private User currentUser(HttpSession session) {
        return (User) session.getAttribute(LOGIN_USER);
    }
}
