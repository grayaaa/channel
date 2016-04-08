package com.netease.channel.entity;

import com.google.common.base.Splitter;
import com.google.common.collect.Sets;

import java.io.Serializable;
import java.util.Set;

public class Role implements Serializable {
    private Long rid; //编号
    private String role; //角色标识 程序中判断使用,如"admin"
    private String description; //角色描述,UI界面显示使用
    private String permissions; //拥有的权限
    private Boolean available = Boolean.FALSE; //是否可用,如果不可用将不会添加给用户

    @Override
    public String toString() {
        return "Role{" +
                "rid=" + rid +
                ", role='" + role + '\'' +
                ", description='" + description + '\'' +
                ", permissions='" + this.getPermissions() + '\'' +
                ", available=" + available +
                '}';
    }

    public Long getRid() {
        return rid;
    }

    public void setRid(Long rid) {
        this.rid = rid;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPermissions() {
        return permissions;
    }

    public void setPermissions(String permissions) {
        this.permissions = permissions;
    }

    public Set<String> getSetPermissions() {
        return Sets.newHashSet(Splitter.on(",").omitEmptyStrings().trimResults().split(this.permissions));
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }
}
