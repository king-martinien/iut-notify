package com.kingmartinien.iutnotifyapi.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDto {

    private String accessToken;

    private String refreshToken;

}
