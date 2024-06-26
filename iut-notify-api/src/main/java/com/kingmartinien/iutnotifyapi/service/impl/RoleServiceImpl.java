package com.kingmartinien.iutnotifyapi.service.impl;

import com.kingmartinien.iutnotifyapi.entity.Permission;
import com.kingmartinien.iutnotifyapi.entity.Role;
import com.kingmartinien.iutnotifyapi.enums.RoleEnum;
import com.kingmartinien.iutnotifyapi.repository.RoleRepository;
import com.kingmartinien.iutnotifyapi.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public Role createRole(RoleEnum roleEnum, Set<Permission> permissions) {
        Role role = new Role();
        role.setLabel(roleEnum);
        role.setPermissions(permissions);
        return this.roleRepository.save(role);
    }

}
