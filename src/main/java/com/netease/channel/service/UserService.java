package com.netease.channel.service;

import com.netease.channel.entity.Role;
import com.netease.channel.entity.User;

import java.util.List;
import java.util.Set;

public interface UserService {
	
	public User getUserByEmail(String email);

    public List<User> getUserList();

    public List<Role> getRoleList();

    public Role getRoleByName(String role);

    public Set<String> getPermissionsByEmail(String email);

    public Set<String> getPermissionsByUser(User user);

    public void addRole(Role role);

    public void addUser(User user);

    public void deleteUser(String email);

    public void deleteRole(String role);
	
}
