package com.visma.kalmar.api.adapters.role;

import com.visma.kalmar.api.entities.role.Role;
import com.visma.kalmar.api.exception.ResourceNotFoundException;
import com.visma.kalmar.api.role.RoleGateway;
import com.visma.feature.kalmar.api.role.RoleRepository;

import java.util.UUID;

public class RoleGatewayAdapter implements RoleGateway {

    private final RoleRepository roleRepository;

    public RoleGatewayAdapter(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role save(Role role) {
        var roleEntity = new com.visma.feature.kalmar.api.role.Role(
                role.idRole(),
                role.name(),
                role.invariantKey(),
                role.description(),
                role.recordVersion(),
                role.whenEdited()
        );

        var savedRole = roleRepository.save(roleEntity);

        return new Role(
                savedRole.getIdRole(),
                savedRole.getName(),
                savedRole.getInvariantKey(),
                savedRole.getDescription(),
                savedRole.getRecordVersion(),
                savedRole.getWhenEdited()
        );
    }

    @Override
    public Role findById(UUID roleId) {
        return roleRepository.findById(roleId)
                .map(this::toDomainRole)
                .orElseThrow(() -> new ResourceNotFoundException("Role", "Role not found with id: " + roleId));
    }

    @Override
    public Role findByInvariantKey(String invariantKey) {
        return roleRepository.findByInvariantKey(invariantKey)
                .map(this::toDomainRole)
                .orElseThrow(() -> new ResourceNotFoundException("Role", "Role not found with invariantKey: " + invariantKey));
    }

    @Override
    public Role update(Role role) {
        roleRepository.findById(role.idRole())
                .orElseThrow(() -> new ResourceNotFoundException("Role", "Role not found with id: " + role.idRole()));

        var roleEntity = new com.visma.feature.kalmar.api.role.Role(
                role.idRole(),
                role.name(),
                role.invariantKey(),
                role.description(),
                role.recordVersion(),
                role.whenEdited()
        );

        var updatedRole = roleRepository.save(roleEntity);

        return new Role(
                updatedRole.getIdRole(),
                updatedRole.getName(),
                updatedRole.getInvariantKey(),
                updatedRole.getDescription(),
                updatedRole.getRecordVersion(),
                updatedRole.getWhenEdited()
        );
    }

    @Override
    public boolean existsByInvariantKey(String invariantKey) {
        return roleRepository.existsByInvariantKey(invariantKey);
    }

    @Override
    public boolean existsByName(String name) {
        return roleRepository.existsByName(name);
    }

    @Override
    public boolean existsById(UUID roleId) {
        return roleRepository.existsById(roleId);
    }

    @Override
    public void deleteById(UUID roleId) {
        var role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role", "Role not found with id: " + roleId));

        roleRepository.delete(role);
    }

    private Role toDomainRole(com.visma.feature.kalmar.api.role.Role roleEntity) {
        return new Role(
                roleEntity.getIdRole(),
                roleEntity.getName(),
                roleEntity.getInvariantKey(),
                roleEntity.getDescription(),
                roleEntity.getRecordVersion(),
                roleEntity.getWhenEdited()
        );
    }
}
