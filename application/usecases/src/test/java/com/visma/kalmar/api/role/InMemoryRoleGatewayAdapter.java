package com.visma.kalmar.api.role;

import com.visma.kalmar.api.entities.role.Role;
import com.visma.kalmar.api.exception.ResourceNotFoundException;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryRoleGatewayAdapter implements RoleGateway {

    private final Map<UUID, Role> database = new ConcurrentHashMap<>();

    @Override
    public Role save(Role role) {
        UUID id = role.idRole();
        if (id == null) {
            id = UUID.randomUUID();
            role = new Role(
                    id,
                    role.name(),
                    role.invariantKey(),
                    role.description(),
                    role.recordVersion(),
                    role.whenEdited()
            );
        }
        database.put(id, role);
        return role;
    }

    @Override
    public Role findById(UUID roleId) {
        Role role = database.get(roleId);
        if (role == null) {
            throw new ResourceNotFoundException("Role", "Role not found with id: " + roleId);
        }
        return role;
    }

    @Override
    public Role findByInvariantKey(String invariantKey) {
        return database.values().stream()
                .filter(role -> role.invariantKey().equals(invariantKey))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Role", "Role not found with invariantKey: " + invariantKey));
    }

    @Override
    public Role update(Role role) {
        if (!database.containsKey(role.idRole())) {
            throw new ResourceNotFoundException("Role", "Role not found with id: " + role.idRole());
        }
        database.put(role.idRole(), role);
        return role;
    }

    @Override
    public boolean existsByInvariantKey(String invariantKey) {
        return database.values().stream()
                .anyMatch(role -> role.invariantKey().equals(invariantKey));
    }

    @Override
    public boolean existsByName(String name) {
        return database.values().stream()
                .anyMatch(role -> role.name().equals(name));
    }

    @Override
    public boolean existsById(UUID roleId) {
        return database.containsKey(roleId);
    }

    @Override
    public void deleteById(UUID roleId) {
        if (!database.containsKey(roleId)) {
            throw new ResourceNotFoundException("Role", "Role not found with id: " + roleId);
        }
        database.remove(roleId);
    }

    public void clear() {
        database.clear();
    }
}
