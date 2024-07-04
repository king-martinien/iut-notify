package com.kingmartinien.iutnotifyapi.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserDto {

    @NotEmpty(message = "Firstname is required")
    @NotBlank(message = "Firstname is required")
    private String firstName;

    @NotEmpty(message = "Lastname is required")
    @NotBlank(message = "Lastname is required")
    private String lastName;

    @NotEmpty(message = "Email is required")
    @NotBlank(message = "Email is required")
    @Email(message = "Email is not valid")
    private String email;

    @NotEmpty(message = "Phone is required")
    @NotBlank(message = "Phone is required")
    private String phone;

    @NotEmpty(message = "Password is required")
    @NotBlank(message = "Password is required")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[@#$%^&+=!]).{8,}$",
            message = "The password must contain at least 8 characters, including an uppercase letter and a special character (@#$%^&+=!).")
    private String password;

    @NotEmpty(message = "List of roles must not be empty")
    private Set<@Valid RoleDto> roles;

}
