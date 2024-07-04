package com.kingmartinien.iutnotifyapi.dto;

import com.kingmartinien.iutnotifyapi.enums.RoleEnum;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleDto {

    private Long id;

    private RoleEnum label;

    private Set<PermissionDto> permissions;

}
