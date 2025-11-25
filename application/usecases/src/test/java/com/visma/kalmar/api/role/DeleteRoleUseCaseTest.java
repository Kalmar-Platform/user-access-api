package com.visma.kalmar.api.role;

import com.visma.kalmar.api.entities.role.Role;
import com.visma.kalmar.api.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class DeleteRoleUseCaseTest {

    private static final UUID ROLE_ID = UUID.randomUUID();
    private static final String NAME = "Administrator";
    private static final String INVARIANT_KEY = "ADMIN";
    private static final String DESCRIPTION = "Full system access";

    private InMemoryRoleGatewayAdapter roleGateway;
    private DeleteRoleUseCase useCase;

    @BeforeEach
    void setUp() {
        roleGateway = new InMemoryRoleGatewayAdapter();
        useCase = new DeleteRoleUseCase(roleGateway);

        var existingRole = new Role(
                ROLE_ID,
                NAME,
                INVARIANT_KEY,
                DESCRIPTION,
                1L,
                new Date()
        );
        roleGateway.save(existingRole);
    }

    @Test
    void deleteRole_withValidId_deletesRoleSuccessfully() {
        assertTrue(roleGateway.existsById(ROLE_ID));

        useCase.deleteRole(ROLE_ID);

        assertFalse(roleGateway.existsById(ROLE_ID));
    }

    @Test
    void deleteRole_withNonExistentId_throwsResourceNotFoundException() {
        UUID nonExistentId = UUID.randomUUID();

        var exception = assertThrows(ResourceNotFoundException.class,
                () -> useCase.deleteRole(nonExistentId));

        assertTrue(exception.getMessage().contains("Role not found with id: " + nonExistentId));
    }

    @Test
    void deleteRole_afterDeletion_cannotFindRole() {
        useCase.deleteRole(ROLE_ID);

        assertThrows(ResourceNotFoundException.class,
                () -> roleGateway.findById(ROLE_ID));
    }

    @Test
    void deleteRole_doesNotAffectOtherRoles() {
        UUID anotherRoleId = UUID.randomUUID();
        var anotherRole = new Role(
                anotherRoleId,
                "User",
                "USER",
                "Regular user access",
                1L,
                new Date()
        );
        roleGateway.save(anotherRole);

        useCase.deleteRole(ROLE_ID);

        assertTrue(roleGateway.existsById(anotherRoleId));
        assertDoesNotThrow(() -> roleGateway.findById(anotherRoleId));
    }
}
