package com.kingmartinien.iutnotifyapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Long id;

    private String firstName;

    private String lastName;

    private String fullName;

    private String email;

    private String phone;

    private Boolean enabled;

    private Boolean accountLocked;

    private Set<RoleDto> roles;

}
