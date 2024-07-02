package com.kingmartinien.iutnotifyapi.controller;

import com.kingmartinien.iutnotifyapi.dto.PublicationDto;
import com.kingmartinien.iutnotifyapi.mapper.PublicationMapper;
import com.kingmartinien.iutnotifyapi.service.PublicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("channels/{channelId}/publications")
@RequiredArgsConstructor
public class PublicationController {

    private final PublicationService publicationService;
    private final PublicationMapper publicationMapper;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<PublicationDto> getAllPublicationByChannelId(@PathVariable("channelId") Long channelId) {
        return this.publicationMapper.toDto(
                this.publicationService.getAllPublicationsByChannelId(channelId));
    }

    @GetMapping("{publicationId}")
    @ResponseStatus(HttpStatus.OK)
    public PublicationDto getPublicationById(
            @PathVariable("channelId") Long channelId,
            @PathVariable("publicationId") Long publicationId) {
        return this.publicationMapper.toDto(
                this.publicationService.getPublicationById(channelId, publicationId));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('PERMISSION_CREATE_PUBLICATIONS')")
    public PublicationDto createPublication(
            @PathVariable(name = "channelId") Long channelId,
            @RequestPart(name = "publication") PublicationDto publicationDto,
            @RequestPart(name = "files") List<MultipartFile> files) {
        return this.publicationMapper.toDto(
                this.publicationService.createPublication(
                        channelId, this.publicationMapper.toEntity(publicationDto), files));
    }

    @PutMapping("{publicationId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('PERMISSION_UPDATE_PUBLICATIONS')")
    public PublicationDto updatePublication(
            @PathVariable(name = "channelId") Long channelId,
            @PathVariable(name = "publicationId") Long publicationId,
            @RequestPart(name = "publication") PublicationDto publicationDto,
            @RequestPart(name = "files") List<MultipartFile> files) {
        return this.publicationMapper.toDto(
                this.publicationService.updatePublication(
                        channelId, publicationId, this.publicationMapper.toEntity(publicationDto), files));
    }

    @DeleteMapping("{publicationId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('PERMISSION_DELETE_PUBLICATIONS')")
    public void deletePublication(
            @PathVariable(name = "channelId") Long channelId,
            @PathVariable(name = "publicationId") Long publicationId) {
        this.publicationService.deletePublication(channelId, publicationId);
    }

}
