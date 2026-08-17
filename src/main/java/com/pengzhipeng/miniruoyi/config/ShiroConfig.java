package com.pengzhipeng.miniruoyi.config;

import com.pengzhipeng.miniruoyi.security.JsonAuthenticationFilter;
import com.pengzhipeng.miniruoyi.security.UserRealm;
import com.pengzhipeng.miniruoyi.service.UserService;
import jakarta.servlet.Filter;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {

    @Bean
    public UserRealm userRealm(UserService userService) {
        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher("SHA-256");
        matcher.setHashIterations(1024);
        matcher.setStoredCredentialsHexEncoded(true);

        UserRealm realm = new UserRealm(userService);
        realm.setCredentialsMatcher(matcher);
        return realm;
    }

    @Bean
    public DefaultWebSessionManager sessionManager() {
        SimpleCookie cookie = new SimpleCookie("MINI_RUOYI_SESSION");
        cookie.setHttpOnly(true);
        cookie.setPath("/");

        DefaultWebSessionManager manager = new DefaultWebSessionManager();
        manager.setSessionIdCookie(cookie);
        manager.setGlobalSessionTimeout(30 * 60 * 1000L);
        manager.setSessionValidationSchedulerEnabled(false);
        return manager;
    }

    @Bean
    public SecurityManager securityManager(UserRealm userRealm, DefaultWebSessionManager sessionManager) {
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
        manager.setRealm(userRealm);
        manager.setSessionManager(sessionManager);
        manager.setRememberMeManager(null);
        return manager;
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(
            @Qualifier("securityManager") SecurityManager securityManager) {
        ShiroFilterFactoryBean factory = new ShiroFilterFactoryBean();
        factory.setSecurityManager(securityManager);

        Map<String, Filter> filters = new LinkedHashMap<>();
        filters.put("jsonAuthc", new JsonAuthenticationFilter());
        factory.setFilters(filters);

        LinkedHashMap<String, String> chain = new LinkedHashMap<>();
        chain.put("/", "anon");
        chain.put("/index.html", "anon");
        chain.put("/favicon.ico", "anon");
        chain.put("/api/login", "anon");
        chain.put("/error", "anon");
        chain.put("/api/**", "jsonAuthc");
        chain.put("/**", "anon");
        factory.setFilterChainDefinitionMap(chain);
        return factory;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(
            @Qualifier("securityManager") SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }
}
