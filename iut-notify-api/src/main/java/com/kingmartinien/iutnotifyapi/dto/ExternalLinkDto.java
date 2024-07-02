package com.kingmartinien.iutnotifyapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExternalLinkDto {

    private Long id;

    @NotEmpty(message = "Url is required")
    @NotBlank(message = "Url is required")
    private String url;

}
