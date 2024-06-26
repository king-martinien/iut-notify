package com.kingmartinien.iutnotifyapi.dto;

import jakarta.validation.constraints.Email;
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
public class LoginRequestDto {

    @NotEmpty(message = "Email is required")
    @NotBlank(message = "Email is required")
    @Email(message = "Email is not valid")
    private String email;

    @NotEmpty(message = "Password is required")
    @NotBlank(message = "Password is required")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[@#$%^&+=!]).{8,}$",
            message = "The password must contain at least 8 characters, including an uppercase letter and a special character (@#$%^&+=!).")
    private String password;

}
