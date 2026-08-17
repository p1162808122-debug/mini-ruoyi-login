package com.pengzhipeng.miniruoyi.security;

import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.shiro.web.filter.AccessControlFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 未登录访问 /api/** 时直接返回 401 JSON，不跳转到另一张页面。
 */
public class JsonAuthenticationFilter extends AccessControlFilter {

    @Override
    protected boolean isAccessAllowed(
            ServletRequest request,
            ServletResponse response,
            Object mappedValue) {
        return getSubject(request, response).isAuthenticated();
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        httpResponse.setCharacterEncoding(StandardCharsets.UTF_8.name());
        httpResponse.setContentType("application/json;charset=UTF-8");
        httpResponse.getWriter().write(
                "{\"code\":401,\"message\":\"未登录或登录已过期\",\"data\":null}"
        );
        return false;
    }
}

