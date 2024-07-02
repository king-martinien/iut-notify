package com.kingmartinien.iutnotifyapi.service;

import com.kingmartinien.iutnotifyapi.entity.Publication;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PublicationService {

    List<Publication> getAllPublicationsByChannelId(Long channelId);

    Publication getPublicationById(Long channelId, Long publicationId);

    Publication createPublication(Long channelId, Publication publication, List<MultipartFile> files);

    Publication updatePublication(Long channelId, Long publicationId, Publication publication, List<MultipartFile> files);

    void deletePublication(Long channelId, Long publicationId);

}
