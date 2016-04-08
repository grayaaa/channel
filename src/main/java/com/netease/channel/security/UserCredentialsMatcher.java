package com.netease.channel.security;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.PasswordMatcher;

public class UserCredentialsMatcher extends PasswordMatcher {

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        if (token instanceof UrsToken) {
            return token.getCredentials().equals(info.getCredentials());
        }
        return super.doCredentialsMatch(token, info);
    }

}
