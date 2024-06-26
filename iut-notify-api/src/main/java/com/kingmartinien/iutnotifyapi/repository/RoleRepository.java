package com.kingmartinien.iutnotifyapi.repository;

import com.kingmartinien.iutnotifyapi.entity.Role;
import com.kingmartinien.iutnotifyapi.enums.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByLabel(RoleEnum label);

}
