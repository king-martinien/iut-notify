package com.kingmartinien.iutnotifyapi.service.impl;

import com.kingmartinien.iutnotifyapi.entity.Permission;
import com.kingmartinien.iutnotifyapi.enums.PermissionEnum;
import com.kingmartinien.iutnotifyapi.repository.PermissionRepository;
import com.kingmartinien.iutnotifyapi.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository permissionRepository;

    @Override
    public Permission createPermission(PermissionEnum label) {
        Permission permission = new Permission();
        permission.setLabel(label);
        return this.permissionRepository.save(permission);
    }

}
