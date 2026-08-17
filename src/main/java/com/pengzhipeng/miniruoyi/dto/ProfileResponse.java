package com.pengzhipeng.miniruoyi.dto;

import com.pengzhipeng.miniruoyi.domain.User;

/**
 * 专门返回给前端的安全对象，不包含 passwordHash 和 salt。
 */
public class ProfileResponse {

    private final Long id;
    private final String username;
    private final String displayName;
    private final String role;
    private final String permission;

    public ProfileResponse(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.displayName = user.getDisplayName();
        this.role = user.getRoleKey();
        this.permission = user.getPermission();
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getRole() {
        return role;
    }

    public String getPermission() {
        return permission;
    }
}

