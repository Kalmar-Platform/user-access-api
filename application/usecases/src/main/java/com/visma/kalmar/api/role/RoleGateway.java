package com.visma.kalmar.api.role;

import com.visma.kalmar.api.entities.role.Role;

import java.util.UUID;

public interface RoleGateway {

    Role save(Role role);

    Role findById(UUID roleId);

    Role findByInvariantKey(String invariantKey);

    Role update(Role role);

    boolean existsByInvariantKey(String invariantKey);

    boolean existsByName(String name);

    boolean existsById(UUID roleId);

    void deleteById(UUID roleId);
}
