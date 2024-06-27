package com.kingmartinien.iutnotifyapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokenDto {

    @NotEmpty(message = "Refresh token is required")
    @NotBlank(message = "Refresh token is required")
    private String refreshToken;

}
