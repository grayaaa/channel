package com.netease.channel.dao;

import com.netease.channel.entity.ChannelGroup;


public interface GroupDao {

    public int getNextID(String tableName);

    public ChannelGroup getGroupByID(int groupId);

   }
