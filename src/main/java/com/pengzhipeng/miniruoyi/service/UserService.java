package com.pengzhipeng.miniruoyi.service;

import com.pengzhipeng.miniruoyi.domain.User;
import com.pengzhipeng.miniruoyi.mapper.UserMapper;
import org.springframework.stereotype.Service;

@Service
public class UserService {

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
}

