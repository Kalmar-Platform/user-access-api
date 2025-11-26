package com.visma.kalmar.api.role;

public record RoleInputData(
        String roleId,
        String name,
        String invariantKey,
        String description
) {
}
