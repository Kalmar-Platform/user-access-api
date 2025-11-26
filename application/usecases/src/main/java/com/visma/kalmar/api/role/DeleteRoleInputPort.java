package com.visma.kalmar.api.role;

import java.util.UUID;

public interface DeleteRoleInputPort {
    void deleteRole(UUID roleId);
}
