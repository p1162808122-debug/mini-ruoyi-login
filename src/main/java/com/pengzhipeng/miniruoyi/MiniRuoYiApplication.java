package com.pengzhipeng.miniruoyi;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.pengzhipeng.miniruoyi.mapper")
@SpringBootApplication
public class MiniRuoYiApplication {

    public static void main(String[] args) {
        SpringApplication.run(MiniRuoYiApplication.class, args);
    }
}

