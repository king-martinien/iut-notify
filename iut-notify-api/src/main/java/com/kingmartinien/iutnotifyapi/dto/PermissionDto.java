package com.kingmartinien.iutnotifyapi.dto;

import com.kingmartinien.iutnotifyapi.enums.PermissionEnum;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PermissionDto {

    @NotEmpty(message = "Permission id is required")
    private Long id;

    @NotEmpty(message = "Permission label is required")
    private PermissionEnum label;

}
