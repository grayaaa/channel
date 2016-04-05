package com.netease.channel.dao;

import com.netease.channel.entity.Role;
import com.netease.channel.entity.User;

import java.util.List;

public interface UserDao {
	
	public User getUserByEmail(String email);

    public List<User> getUserList();

    public Role getRoleByName(String role);

    public List<Role> getRoleList();

    public void addRole(Role role);

    public void addUser(User user);

    public void deleteUser(String email);

    public void deleteRole(String role);

}
