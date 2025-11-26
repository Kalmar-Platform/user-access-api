package com.visma.kalmar.api.role;

import com.visma.kalmar.api.entities.role.Role;
import com.visma.kalmar.api.exception.ResourceAlreadyExistsException;

import java.util.Date;

public class CreateRoleUseCase implements CreateRoleInputPort {

    private static final Long RECORD_VERSION = 1L;
    private final RoleGateway roleGateway;

    public CreateRoleUseCase(RoleGateway roleGateway) {
        this.roleGateway = roleGateway;
    }

    @Override
    public void createRole(RoleInputData inputData, RoleOutputPort outputPort) {
        if (roleGateway.existsByInvariantKey(inputData.invariantKey())) {
            throw new ResourceAlreadyExistsException("Role", "Role with invariantKey: " + inputData.invariantKey() + " already exists.");
        }

        if (roleGateway.existsByName(inputData.name())) {
            throw new ResourceAlreadyExistsException("Role", "Role with name: " + inputData.name() + " already exists.");
        }

        var role = new Role(
                null,
                inputData.name(),
                inputData.invariantKey(),
                inputData.description(),
                RECORD_VERSION,
                new Date()
        );

        var savedRole = roleGateway.save(role);

        var outputData = new RoleOutputData(
                savedRole.idRole().toString(),
                savedRole.name(),
                savedRole.invariantKey(),
                savedRole.description(),
                true
        );

        outputPort.present(outputData);
    }
}
