package com.kingmartinien.iutnotifyapi.service;

import com.kingmartinien.iutnotifyapi.entity.Permission;
import com.kingmartinien.iutnotifyapi.enums.PermissionEnum;

public interface PermissionService {

    Permission createPermission(PermissionEnum label);

}
