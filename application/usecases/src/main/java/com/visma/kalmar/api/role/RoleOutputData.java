package com.visma.kalmar.api.role;

public record RoleOutputData(
        String roleId,
        String name,
        String invariantKey,
        String description,
        boolean created
) {
}
