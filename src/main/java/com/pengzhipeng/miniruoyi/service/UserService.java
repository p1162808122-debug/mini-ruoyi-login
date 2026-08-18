package com.pengzhipeng.miniruoyi.service;

import com.pengzhipeng.miniruoyi.domain.User;
import com.pengzhipeng.miniruoyi.mapper.UserMapper;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
public class UserService {

    private static final int HASH_ITERATIONS = 1024;

    private final UserMapper userMapper;

    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    /**
     * 业务层负责整理用户名，再把查询任务交给 Mapper。
     */
    public User findByUsername(String username) {
        if (username == null || username.isBlank()) {
            return null;
        }
        return userMapper.selectByUsername(username.trim());
    }

    /**
     * 登录认证也属于业务层：查询用户、检查状态、校验密码。
     */
    public User authenticate(String username, String password) {
        User user = findByUsername(username);
        if (user == null || !user.isEnabled() || password == null) {
            return null;
        }

        String actualHash = hashPassword(password, user.getSalt());
        byte[] expected = user.getPasswordHash().getBytes(StandardCharsets.UTF_8);
        byte[] actual = actualHash.getBytes(StandardCharsets.UTF_8);
        return MessageDigest.isEqual(expected, actual) ? user : null;
    }

    /**
     * 与原教学项目保持一致：盐 + SHA-256，连续计算 1024 次。
     */
    private String hashPassword(String password, String salt) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] value = (salt + password).getBytes(StandardCharsets.UTF_8);
            for (int i = 0; i < HASH_ITERATIONS; i++) {
                value = digest.digest(value);
            }
            return java.util.HexFormat.of().formatHex(value);
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException("SHA-256 不可用", exception);
        }
    }
}
