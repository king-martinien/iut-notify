package com.kingmartinien.iutnotifyapi.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PublicationDto {

    private Long id;

    @NotEmpty(message = "Title is required")
    @NotBlank(message = "Title is required")
    private String title;

    @NotEmpty(message = "Content is required")
    @NotBlank(message = "Content is required")
    private String content;

    @NotEmpty(message = "Publication type is required")
    @NotBlank(message = "Publication type is required")
    private String publicationType;

    @Valid
    private Set<ExternalLinkDto> externalLinks;

    private Set<AttachmentDto> attachments;

}
