package com.kingmartinien.iutnotifyapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChannelDto {

    private Long id;

    @NotEmpty(message = "Channel name is required")
    @NotBlank(message = "Channel name is required")
    private String name;

    @NotEmpty(message = "Channel description is required")
    @NotBlank(message = "Channel description is required")
    private String description;

}
