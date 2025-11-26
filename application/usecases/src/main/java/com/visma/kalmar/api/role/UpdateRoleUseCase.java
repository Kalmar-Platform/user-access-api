package com.visma.kalmar.api.role;

import com.visma.kalmar.api.entities.role.Role;

import java.util.Date;
import java.util.UUID;

public class UpdateRoleUseCase implements UpdateRoleInputPort {

    private final RoleGateway roleGateway;

    public UpdateRoleUseCase(RoleGateway roleGateway) {
        this.roleGateway = roleGateway;
    }

    @Override
    public void updateRole(RoleInputData inputData, RoleOutputPort outputPort) {
        if (inputData.roleId() == null || inputData.roleId().isEmpty()) {
            throw new IllegalArgumentException("Role ID is required for update operation");
        }

        UUID roleId = UUID.fromString(inputData.roleId());

        var existingRole = roleGateway.findById(roleId);

        var roleToUpdate = new Role(
                roleId,
                inputData.name(),
                inputData.invariantKey(),
                inputData.description(),
                existingRole.recordVersion(),
                new Date()
        );

        var updatedRole = roleGateway.update(roleToUpdate);

        var outputData = new RoleOutputData(
                updatedRole.idRole().toString(),
                updatedRole.name(),
                updatedRole.invariantKey(),
                updatedRole.description(),
                false
        );

        outputPort.present(outputData);
    }
}
