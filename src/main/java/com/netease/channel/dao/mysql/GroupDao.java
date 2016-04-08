package com.netease.channel.dao.mysql;

import com.netease.channel.entity.ChannelGroup;


public interface GroupDao {

    public ChannelGroup getGroupByID(int groupId);

}
