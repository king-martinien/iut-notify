package com.kingmartinien.iutnotifyapi.service;

import com.kingmartinien.iutnotifyapi.entity.ExternalLink;
import com.kingmartinien.iutnotifyapi.entity.Publication;

import java.util.List;

public interface ExternalLinkService {

    void createExternalLink(Publication publication, ExternalLink externalLink);

    void createExtraLinks(Publication publication, List<ExternalLink> externalLinks);

}
