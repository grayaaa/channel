package com.netease.channel.security;

import com.netease.channel.entity.User;
import com.netease.channel.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.Set;

public class ShiroDbRealm extends AuthorizingRealm {


    @Resource
    private UserService userService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(
            PrincipalCollection principals) {
        if (principals == null) {
            throw new AuthorizationException("参数不能为空");
        }
        User user = (User) getAvailablePrincipal(principals);
        user = userService.getUserByEmail(user.getEmail());
        logger.info("login user-->" + user.toString());

        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        Set<String> permissions = userService.getPermissionsByUser(user);
        logger.info("permissions-->" + permissions.toString());
        authorizationInfo.setRoles(user.getSetRoles());
        authorizationInfo.setStringPermissions(permissions);

        return authorizationInfo;
    }

    //认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(
            AuthenticationToken token) throws AuthenticationException {
        if (token instanceof UrsToken) {
            UrsToken ursToken = (UrsToken) token;
            User user = userService.getUserByEmail(((UrsToken) token).getAccount());
            if (user == null) {
                throw new AccountException("用户不存在");
            }
            return new UrsAuthenticationInfo(user, ursToken.getAccount());
        }
        throw new AccountException("不支持的 AuthenticationToken");
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        return token != null && (UsernamePasswordToken.class.isAssignableFrom(token.getClass()) || UrsToken.class.isAssignableFrom(token.getClass()));
    }


    /**
     * 使用email作为用户权限缓存的key
     */
    @Override
    protected Object getAuthorizationCacheKey(PrincipalCollection principals) {
        User user = (User) getAvailablePrincipal(principals);
        return user.getEmail();
    }

    /**
     * 删除用户权限缓存
     *
     * @param username
     */
    public void removeUsersAuthorizationCache(String... username) {
        for (String name : username) {
            getAuthorizationCache().remove(name);
        }
    }

}
