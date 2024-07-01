package com.kingmartinien.iutnotifyapi.controller;

import com.kingmartinien.iutnotifyapi.dto.ChannelDto;
import com.kingmartinien.iutnotifyapi.mapper.ChannelMapper;
import com.kingmartinien.iutnotifyapi.service.ChannelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("channels")
@RequiredArgsConstructor
public class ChannelController {

    private final ChannelService channelService;
    private final ChannelMapper channelMapper;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ChannelDto> getAllChannels() {
        return this.channelMapper.toDto(this.channelService.getAllChannels());
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public ChannelDto getChannelById(@PathVariable Long id) {
        return this.channelMapper.toDto(this.channelService.getChannelById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('PERMISSION_CREATE_CHANNEL')")
    public ChannelDto createChannel(@RequestBody @Valid ChannelDto channelDto) {
        return this.channelMapper.toDto(
                this.channelService.createChannel(
                        this.channelMapper.toEntity(channelDto)));
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('PERMISSION_UPDATE_CHANNEL')")
    public ChannelDto updateChannel(@PathVariable Long id, @RequestBody @Valid ChannelDto channelDto) {
        return this.channelMapper.toDto(
                this.channelService.updateChannel(id, this.channelMapper.toEntity(channelDto)));
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('PERMISSION_DELETE_CHANNEL')")
    public void deleteChannel(@PathVariable Long id) {
        this.channelService.deleteChannel(id);
    }

}
