package com.kingmartinien.iutnotifyapi.service;

import com.kingmartinien.iutnotifyapi.entity.Publication;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface AttachmentService {

    void createAttachment(Publication publication, MultipartFile file) throws IOException;

    void createAttachments(Publication publication, List<MultipartFile> files);

}
