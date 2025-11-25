package com.visma.kalmar.api.role;

import java.util.UUID;

public interface GetRoleInputPort {
    void getRole(UUID roleId, RoleOutputPort outputPort);
}
