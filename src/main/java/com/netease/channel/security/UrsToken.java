package com.netease.channel.security;

import org.apache.shiro.authc.AuthenticationToken;

public class UrsToken implements AuthenticationToken {

    private static final long serialVersionUID = 5087057947160393171L;
    private String account;

    public UrsToken(String account) {
        this.account = account;
    }

    @Override
    public Object getPrincipal() {
        return getAccount();
    }

    @Override
    public Object getCredentials() {
        return getAccount();
    }

    public String getAccount() {
        return this.account;
    }

}
