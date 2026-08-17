package com.pengzhipeng.miniruoyi.security;

import com.pengzhipeng.miniruoyi.domain.User;
import com.pengzhipeng.miniruoyi.service.UserService;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.lang.util.ByteSource;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

/**
 * Realm 是 Shiro 与我们自己的用户数据之间的桥梁。
 */
public class UserRealm extends AuthorizingRealm {

    private final UserService userService;

    public UserRealm(UserService userService) {
        this.userService = userService;
    }

    /**
     * 当代码检查角色或权限时，Shiro 会回调这里。
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        User user = (User) principals.getPrimaryPrincipal();
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.addRole(user.getRoleKey());
        info.addStringPermission(user.getPermission());
        return info;
    }

    /**
     * Controller 调用 subject.login(token) 后，Shiro 会回调这里。
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) {
        String username = String.valueOf(token.getPrincipal());
        User user = userService.findByUsername(username);

        if (user == null) {
            throw new UnknownAccountException("用户名或密码错误");
        }
        if (!user.isEnabled()) {
            throw new LockedAccountException("账号已停用");
        }

        return new SimpleAuthenticationInfo(
                user,
                user.getPasswordHash(),
                ByteSource.Util.bytes(user.getSalt()),
                getName()
        );
    }
}

