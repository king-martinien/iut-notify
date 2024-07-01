package com.kingmartinien.iutnotifyapi.mapper;

import com.kingmartinien.iutnotifyapi.dto.ChannelDto;
import com.kingmartinien.iutnotifyapi.entity.Channel;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ChannelMapper {

    ChannelDto toDto(Channel channel);

    List<ChannelDto> toDto(List<Channel> channels);

    Channel toEntity(ChannelDto channelDto);

}
