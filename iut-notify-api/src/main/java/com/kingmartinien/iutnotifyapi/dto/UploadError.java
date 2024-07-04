package com.kingmartinien.iutnotifyapi.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UploadError {

    private int line;

    private String error;

}
