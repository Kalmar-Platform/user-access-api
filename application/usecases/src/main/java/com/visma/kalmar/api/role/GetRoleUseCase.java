package com.visma.kalmar.api.role;

import java.util.UUID;

public class GetRoleUseCase implements GetRoleInputPort {

    private final RoleGateway roleGateway;

    public GetRoleUseCase(RoleGateway roleGateway) {
        this.roleGateway = roleGateway;
    }

    @Override
    public void getRole(UUID roleId, RoleOutputPort outputPort) {
        var role = roleGateway.findById(roleId);

        var outputData = new RoleOutputData(
                role.idRole().toString(),
                role.name(),
                role.invariantKey(),
                role.description(),
                false
        );

        outputPort.present(outputData);
    }
}
