package com.pengzhipeng.miniruoyi.controller;

import com.pengzhipeng.miniruoyi.common.ApiResponse;
import com.pengzhipeng.miniruoyi.domain.User;
import com.pengzhipeng.miniruoyi.dto.ProfileResponse;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AuthController {

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<ProfileResponse>> login(
            @RequestParam String username,
            @RequestParam String password) {
        Subject subject = SecurityUtils.getSubject();

        try {
            subject.login(new UsernamePasswordToken(username, password));
            User user = (User) subject.getPrincipal();
            return ResponseEntity.ok(
                    ApiResponse.success("登录成功", new ProfileResponse(user))
            );
        } catch (AuthenticationException exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(401, "用户名或密码错误"));
        }
    }

    @GetMapping("/profile")
    @RequiresPermissions("dashboard:view")
    public ApiResponse<ProfileResponse> profile() {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        return ApiResponse.success("已登录", new ProfileResponse(user));
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout() {
        SecurityUtils.getSubject().logout();
        return ApiResponse.success("已退出登录", null);
    }

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<ApiResponse<Void>> handleAuthorizationException() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ApiResponse.error(403, "没有访问这个页面的权限"));
    }
}

