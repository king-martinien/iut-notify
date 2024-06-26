package com.kingmartinien.iutnotifyapi.service;

import com.kingmartinien.iutnotifyapi.entity.Permission;
import com.kingmartinien.iutnotifyapi.entity.Role;
import com.kingmartinien.iutnotifyapi.enums.RoleEnum;

import java.util.Set;

public interface RoleService {

    Role createRole(RoleEnum roleEnum, Set<Permission> permissions);

}
