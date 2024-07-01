package com.kingmartinien.iutnotifyapi.service.impl;

import com.kingmartinien.iutnotifyapi.entity.Channel;
import com.kingmartinien.iutnotifyapi.repository.ChannelReposiroty;
import com.kingmartinien.iutnotifyapi.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChannelServiceImpl implements ChannelService {

    private final ChannelReposiroty channelReposiroty;

    @Override
    public List<Channel> getAllChannels() {
        return this.channelReposiroty.findAll();
    }

    @Override
    public Channel getChannelById(Long id) {
        return this.channelReposiroty.findById(id)
                .orElseThrow(() -> new RuntimeException("Channel not found with id: " + id));
    }

    @Override
    public Channel createChannel(Channel channel) {
        Optional<Channel> foundChannel = this.channelReposiroty.findByName(channel.getName());
        if (foundChannel.isPresent()) {
            throw new RuntimeException("Channel with name " + channel.getName() + " already exists");
        }
        return this.channelReposiroty.save(channel);
    }

    @Override
    public Channel updateChannel(Long channelId, Channel channel) {
        Channel channelToUpdate = this.getChannelById(channelId);
        if (channelToUpdate.getName().equals(channel.getName())) {
            throw new RuntimeException("Channel with name " + channel.getName() + " already exists");
        }
        channelToUpdate.setName(channel.getName());
        channelToUpdate.setDescription(channel.getDescription());
        return this.channelReposiroty.save(channelToUpdate);
    }

    @Override
    public void deleteChannel(Long channelId) {
        Channel channelToDelete = this.getChannelById(channelId);
        this.channelReposiroty.delete(channelToDelete);
    }

}
