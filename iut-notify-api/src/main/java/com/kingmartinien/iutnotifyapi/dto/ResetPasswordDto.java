package com.kingmartinien.iutnotifyapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResetPasswordDto {

    @NotEmpty(message = "Password is required")
    @NotBlank(message = "Password is required")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[@#$%^&+=!]).{8,}$",
            message = "The password must contain at least 8 characters, including an uppercase letter and a special character (@#$%^&+=!).")
    private String password;

}
