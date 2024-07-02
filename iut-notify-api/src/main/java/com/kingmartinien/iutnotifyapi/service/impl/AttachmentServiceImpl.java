package com.kingmartinien.iutnotifyapi.service.impl;

import com.kingmartinien.iutnotifyapi.entity.Attachment;
import com.kingmartinien.iutnotifyapi.entity.Publication;
import com.kingmartinien.iutnotifyapi.firebase.FirebaseStorageService;
import com.kingmartinien.iutnotifyapi.repository.AttachmentRepository;
import com.kingmartinien.iutnotifyapi.service.AttachmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AttachmentServiceImpl implements AttachmentService {

    private final AttachmentRepository attachmentRepository;
    private final FirebaseStorageService firebaseStorageService;

    @Override
    public void createAttachment(Publication publication, MultipartFile file) throws IOException {
        Attachment attachment = Attachment.builder()
                .fileName(file.getOriginalFilename())
                .contentType(file.getContentType())
                .blobId(this.firebaseStorageService.uploadFile(file))
                .publication(publication)
                .build();
        this.attachmentRepository.save(attachment);
    }

    @Override
    public void createAttachments(Publication publication, List<MultipartFile> files) {
        files.forEach(file -> {
            try {
                this.createAttachment(publication, file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

}
