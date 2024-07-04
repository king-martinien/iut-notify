package com.kingmartinien.iutnotifyapi.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UploadResult {

    private int total;

    private int success;

    private List<UploadError> errors;

}
