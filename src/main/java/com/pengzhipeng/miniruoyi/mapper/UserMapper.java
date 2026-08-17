package com.pengzhipeng.miniruoyi.mapper;

import com.pengzhipeng.miniruoyi.domain.User;

/**
 * 这里只有方法声明；真正的 SQL 在 resources/mapper/UserMapper.xml。
 */
public interface UserMapper {

    User selectByUsername(String username);
}

