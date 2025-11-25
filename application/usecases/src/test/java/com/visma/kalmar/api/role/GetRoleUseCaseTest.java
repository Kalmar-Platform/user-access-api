package com.visma.kalmar.api.role;

import com.visma.kalmar.api.entities.role.Role;
import com.visma.kalmar.api.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

class GetRoleUseCaseTest {

    private static final UUID ROLE_ID = UUID.randomUUID();
    private static final String NAME = "Administrator";
    private static final String INVARIANT_KEY = "ADMIN";
    private static final String DESCRIPTION = "Full system access";

    private InMemoryRoleGatewayAdapter roleGateway;
    private GetRoleUseCase useCase;

    @BeforeEach
    void setUp() {
        roleGateway = new InMemoryRoleGatewayAdapter();
        useCase = new GetRoleUseCase(roleGateway);

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
    void getRole_withValidId_retrievesRoleSuccessfully() {
        final AtomicReference<RoleOutputData> result = new AtomicReference<>();

        useCase.getRole(ROLE_ID, output -> result.set(output));

        assertNotNull(result.get());
        assertEquals(ROLE_ID.toString(), result.get().roleId());
        assertEquals(NAME, result.get().name());
        assertEquals(INVARIANT_KEY, result.get().invariantKey());
        assertEquals(DESCRIPTION, result.get().description());
        assertFalse(result.get().created());
    }

    @Test
    void getRole_withNonExistentId_throwsResourceNotFoundException() {
        UUID nonExistentId = UUID.randomUUID();

        var exception = assertThrows(ResourceNotFoundException.class,
                () -> useCase.getRole(nonExistentId, output -> {}));

        assertTrue(exception.getMessage().contains("Role not found with id: " + nonExistentId));
    }

    @Test
    void getRole_returnsCreatedFlagAsFalse() {
        final AtomicReference<RoleOutputData> result = new AtomicReference<>();

        useCase.getRole(ROLE_ID, output -> result.set(output));

        assertNotNull(result.get());
        assertFalse(result.get().created());
    }

    @Test
    void getRole_withNullDescription_returnsRoleWithNullDescription() {
        UUID roleIdWithNullDesc = UUID.randomUUID();
        var roleWithNullDescription = new Role(
                roleIdWithNullDesc,
                "Test Role",
                "TEST",
                null,
                1L,
                new Date()
        );
        roleGateway.save(roleWithNullDescription);

        final AtomicReference<RoleOutputData> result = new AtomicReference<>();

        useCase.getRole(roleIdWithNullDesc, output -> result.set(output));

        assertNotNull(result.get());
        assertEquals(roleIdWithNullDesc.toString(), result.get().roleId());
        assertEquals("Test Role", result.get().name());
        assertEquals("TEST", result.get().invariantKey());
        assertNull(result.get().description());
    }

    @Test
    void getRole_callsOutputPortPresent() {
        final AtomicReference<RoleOutputData> result = new AtomicReference<>();

        useCase.getRole(ROLE_ID, output -> result.set(output));

        assertNotNull(result.get());
    }
}
