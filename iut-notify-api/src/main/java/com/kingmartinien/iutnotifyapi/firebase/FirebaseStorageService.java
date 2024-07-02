package com.kingmartinien.iutnotifyapi.firebase;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FirebaseStorageService {

    private final Bucket bucket;
    private final Storage storage;

    public String uploadFile(MultipartFile file) throws IOException {
        String generatedFilename = UUID.randomUUID() + "." + file.getOriginalFilename();
        InputStream inputStream = file.getInputStream();
        BlobInfo blobInfo = BlobInfo.newBuilder(bucket, generatedFilename)
                .setContentType(file.getContentType())
                .build();
        Blob blob = storage.createFrom(blobInfo, inputStream);
        return blob.getBlobId().getName();
    }

}
