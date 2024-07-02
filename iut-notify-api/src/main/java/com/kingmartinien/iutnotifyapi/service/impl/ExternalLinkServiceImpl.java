package com.kingmartinien.iutnotifyapi.service.impl;

import com.kingmartinien.iutnotifyapi.entity.ExternalLink;
import com.kingmartinien.iutnotifyapi.entity.Publication;
import com.kingmartinien.iutnotifyapi.repository.ExternalLinkRepository;
import com.kingmartinien.iutnotifyapi.service.ExternalLinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExternalLinkServiceImpl implements ExternalLinkService {

    private final ExternalLinkRepository externalLinkRepository;

    @Override
    public void createExternalLink(Publication publication, ExternalLink externalLink) {
        externalLink.setPublication(publication);
        externalLinkRepository.save(externalLink);
    }

    @Override
    public void createExtraLinks(Publication publication, List<ExternalLink> externalLinks) {
        externalLinks.forEach(link -> createExternalLink(publication, link));
    }

}
