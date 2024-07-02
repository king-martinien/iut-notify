package com.kingmartinien.iutnotifyapi.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttachmentDto {

    private Long id;

    private String fileName;

    private String contentType;

    private String blobId;

}
