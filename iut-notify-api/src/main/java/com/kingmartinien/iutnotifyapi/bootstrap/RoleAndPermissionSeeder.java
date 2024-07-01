package com.kingmartinien.iutnotifyapi.bootstrap;

import com.kingmartinien.iutnotifyapi.entity.Permission;
import com.kingmartinien.iutnotifyapi.entity.Role;
import com.kingmartinien.iutnotifyapi.enums.PermissionEnum;
import com.kingmartinien.iutnotifyapi.enums.RoleEnum;
import com.kingmartinien.iutnotifyapi.repository.PermissionRepository;
import com.kingmartinien.iutnotifyapi.repository.RoleRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RoleAndPermissionSeeder implements ApplicationListener<ContextRefreshedEvent> {

    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;


    @Override
    public void onApplicationEvent(@NonNull ContextRefreshedEvent event) {
        Permission readPublications = this.createPermission(PermissionEnum.READ_PUBLICATIONS);
        Permission createPublication = this.createPermission(PermissionEnum.CREATE_PUBLICATIONS);
        Permission updatePublication = this.createPermission(PermissionEnum.UPDATE_PUBLICATIONS);
        Permission deletePublication = this.createPermission(PermissionEnum.DELETE_PUBLICATIONS);
        Permission createChannel = this.createPermission(PermissionEnum.CREATE_CHANNEL);
        Permission updateChannel = this.createPermission(PermissionEnum.UPDATE_CHANNEL);

        this.createRole(RoleEnum.STUDENT, Set.of(readPublications));
        this.createRole(RoleEnum.TEACHER, Set.of(createPublication, updatePublication, deletePublication, createChannel));
        this.createRole(RoleEnum.RA, Set.of(createChannel, updateChannel));
    }

    private void createRole(RoleEnum roleEnum, Set<Permission> permissions) {
        Optional<Role> optionalRole = this.roleRepository.findByLabel(roleEnum);
        if (optionalRole.isEmpty()) {
            Role role = new Role();
            role.setLabel(roleEnum);
            role.setPermissions(permissions);
            this.roleRepository.save(role);
        }
        if (optionalRole.isPresent()) {
            optionalRole.get().setPermissions(permissions);
            this.roleRepository.save(optionalRole.get());
        }
    }

    private Permission createPermission(PermissionEnum permissionEnum) {
        Optional<Permission> optionalPermission = this.permissionRepository.findByLabel(permissionEnum);
        if (optionalPermission.isEmpty()) {
            Permission permission = new Permission();
            permission.setLabel(permissionEnum);
            return this.permissionRepository.save(permission);
        } else {
            return optionalPermission.get();
        }
    }

}
