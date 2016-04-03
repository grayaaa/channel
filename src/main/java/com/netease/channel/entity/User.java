package com.netease.channel.entity;

import com.google.common.base.Splitter;
import com.google.common.collect.Sets;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

public class User implements Serializable {
    private static final long serialVersionUID = 7536449367500922853L;
    private long userId;
    private String email;
    private String name;
    private String roles; //拥有的角色列表
    private String cname;
    private Date ctime;
    private Date ltime;

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", roles='" + this.getSetRoles() + '\'' +
                ", cname='" + cname + '\'' +
                ", ctime=" + ctime +
                ", ltime=" + ltime +
                '}';
    }

    public long getUserId() {

        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public Set<String> getSetRoles() {
        return Sets.newHashSet(Splitter.on(",").omitEmptyStrings().trimResults().split(this.roles));
    }

    public Date getCtime() {
        return ctime;
    }

    public void setCtime(Date ctime) {
        this.ctime = ctime;
    }

    public Date getLtime() {
        return ltime;
    }

    public void setLtime(Date ltime) {
        this.ltime = ltime;
    }
}
