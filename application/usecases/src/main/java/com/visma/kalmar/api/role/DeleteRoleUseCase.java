package com.visma.kalmar.api.role;

import java.util.UUID;

public class DeleteRoleUseCase implements DeleteRoleInputPort {

    private final RoleGateway roleGateway;

    public DeleteRoleUseCase(RoleGateway roleGateway) {
        this.roleGateway = roleGateway;
    }

    @Override
    public void deleteRole(UUID roleId) {
        roleGateway.findById(roleId);

        roleGateway.deleteById(roleId);
    }
}
