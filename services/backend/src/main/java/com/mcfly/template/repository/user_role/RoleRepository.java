package com.mcfly.template.repository.user_role;

import com.mcfly.template.domain.user_role.Role;
import com.mcfly.template.domain.user_role.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(RoleName roleName);
}
