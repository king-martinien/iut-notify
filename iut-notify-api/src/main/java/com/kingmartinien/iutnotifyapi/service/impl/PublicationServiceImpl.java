package com.kingmartinien.iutnotifyapi.service.impl;

import com.kingmartinien.iutnotifyapi.entity.*;
import com.kingmartinien.iutnotifyapi.repository.PublicationRepository;
import com.kingmartinien.iutnotifyapi.service.AttachmentService;
import com.kingmartinien.iutnotifyapi.service.ChannelService;
import com.kingmartinien.iutnotifyapi.service.ExternalLinkService;
import com.kingmartinien.iutnotifyapi.service.PublicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PublicationServiceImpl implements PublicationService {

    private final PublicationRepository publicationRepository;
    private final ChannelService channelService;
    private final ExternalLinkService externalLinkService;
    private final AttachmentService attachmentService;


    @Override
    public List<Publication> getAllPublicationsByChannelId(Long channelId) {
        Channel channel = this.channelService.getChannelById(channelId);
        return this.publicationRepository.findAllByChannelId(channel.getId());
    }

    @Override
    public Publication getPublicationById(Long channelId, Long publicationId) {
        Channel channel = this.channelService.getChannelById(channelId);
        Publication publication = this.publicationRepository.findById(publicationId)
                .orElseThrow(() -> new RuntimeException("Publication not found"));
        if (publication.getChannel().getId().equals(channel.getId())) {
            return publication;
        } else {
            throw new RuntimeException("Publication not belong to this channel");
        }
    }

    @Override
    public Publication createPublication(Long channelId, Publication publication, List<MultipartFile> files) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Channel channel = this.channelService.getChannelById(channelId);
        publication.setChannel(channel);
        publication.setUser(user);
        Publication savedPublication = this.publicationRepository.save(publication);

        if (!publication.getExternalLinks().isEmpty()) {
            List<ExternalLink> externalLinks = publication.getExternalLinks().stream().toList();
            this.externalLinkService.createExtraLinks(savedPublication, externalLinks);
        }

        if (!files.isEmpty()) {
            this.attachmentService.createAttachments(savedPublication, files);
        }

        return this.publicationRepository.findById(savedPublication.getId())
                .orElseThrow(() -> new RuntimeException("Publication not found"));

    }

    @Override
    public Publication updatePublication(Long channelId, Long publicationId, Publication publication, List<MultipartFile> files) {
        Publication publicationToUpdate = this.getPublicationById(channelId, publicationId);
        publicationToUpdate.setTitle(publication.getTitle());
        publicationToUpdate.setContent(publication.getContent());
        return this.publicationRepository.save(publicationToUpdate);
    }

    @Override
    public void deletePublication(Long channelId, Long publicationId) {
        Publication publication = this.getPublicationById(channelId, publicationId);
        this.publicationRepository.delete(publication);
    }

}
