package com.kingmartinien.iutnotifyapi.service;

import com.kingmartinien.iutnotifyapi.entity.Channel;

import java.util.List;

public interface ChannelService {

    List<Channel> getAllChannels();

    Channel getChannelById(Long id);

    Channel createChannel(Channel channel);

    Channel updateChannel(Long channelId, Channel channel);

    void deleteChannel(Long channelId);

}
