package com.netease.channel.security;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;

public class UrsAuthenticationInfo implements AuthenticationInfo {

    private static final long serialVersionUID = -2313707100019207469L;
    private PrincipalCollection principals;
    private String credentials;

    public UrsAuthenticationInfo(Object principal, String realmName) {
        this.principals = new SimplePrincipalCollection(principal, realmName);
        this.credentials = realmName;
    }

    @Override
    public PrincipalCollection getPrincipals() {
        return this.principals;
    }

    @Override
    public Object getCredentials() {
        return this.credentials;
    }

}
