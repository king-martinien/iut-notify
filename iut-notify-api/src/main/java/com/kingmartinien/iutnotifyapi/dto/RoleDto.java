package com.kingmartinien.iutnotifyapi.dto;

import com.kingmartinien.iutnotifyapi.enums.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoleDto {

    private Long id;

    private RoleEnum label;

    private Set<PermissionDto> permissions;

}
