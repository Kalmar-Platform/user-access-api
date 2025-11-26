package com.visma.kalmar.api.role;

import com.visma.kalmar.api.entities.role.Role;
import com.visma.kalmar.api.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

class UpdateRoleUseCaseTest {

    private static final UUID ROLE_ID = UUID.randomUUID();
    private static final String ORIGINAL_NAME = "Administrator";
    private static final String ORIGINAL_INVARIANT_KEY = "ADMIN";
    private static final String ORIGINAL_DESCRIPTION = "Full system access";
    private static final String UPDATED_NAME = "Super Administrator";
    private static final String UPDATED_INVARIANT_KEY = "SUPER_ADMIN";
    private static final String UPDATED_DESCRIPTION = "Extended system access";

    private InMemoryRoleGatewayAdapter roleGateway;
    private UpdateRoleUseCase useCase;

    @BeforeEach
    void setUp() {
        roleGateway = new InMemoryRoleGatewayAdapter();
        useCase = new UpdateRoleUseCase(roleGateway);

        var existingRole = new Role(
                ROLE_ID,
                ORIGINAL_NAME,
                ORIGINAL_INVARIANT_KEY,
                ORIGINAL_DESCRIPTION,
                1L,
                new Date()
        );
        roleGateway.save(existingRole);
    }

    @Test
    void updateRole_withValidData_updatesRoleSuccessfully() {
        var inputData = new RoleInputData(
                ROLE_ID.toString(),
                UPDATED_NAME,
                UPDATED_INVARIANT_KEY,
                UPDATED_DESCRIPTION
        );
        final AtomicReference<RoleOutputData> result = new AtomicReference<>();

        useCase.updateRole(inputData, output -> result.set(output));

        assertNotNull(result.get());
        assertEquals(ROLE_ID.toString(), result.get().roleId());
        assertEquals(UPDATED_NAME, result.get().name());
        assertEquals(UPDATED_INVARIANT_KEY, result.get().invariantKey());
        assertEquals(UPDATED_DESCRIPTION, result.get().description());
        assertFalse(result.get().created());

        var updatedRole = roleGateway.findById(ROLE_ID);
        assertEquals(UPDATED_NAME, updatedRole.name());
        assertEquals(UPDATED_INVARIANT_KEY, updatedRole.invariantKey());
        assertEquals(UPDATED_DESCRIPTION, updatedRole.description());
    }

    @Test
    void updateRole_withNonExistentId_throwsResourceNotFoundException() {
        UUID nonExistentId = UUID.randomUUID();
        var inputData = new RoleInputData(
                nonExistentId.toString(),
                UPDATED_NAME,
                UPDATED_INVARIANT_KEY,
                UPDATED_DESCRIPTION
        );

        var exception = assertThrows(ResourceNotFoundException.class,
                () -> useCase.updateRole(inputData, output -> {
                }));

        assertTrue(exception.getMessage().contains("Role not found with id: " + nonExistentId));
    }

    @Test
    void updateRole_withNullRoleId_throwsIllegalArgumentException() {
        var inputData = new RoleInputData(
                null,
                UPDATED_NAME,
                UPDATED_INVARIANT_KEY,
                UPDATED_DESCRIPTION
        );

        assertThrows(IllegalArgumentException.class,
                () -> useCase.updateRole(inputData, output -> {
                }));
    }

    @Test
    void updateRole_withEmptyRoleId_throwsIllegalArgumentException() {
        var inputData = new RoleInputData(
                "",
                UPDATED_NAME,
                UPDATED_INVARIANT_KEY,
                UPDATED_DESCRIPTION
        );

        assertThrows(IllegalArgumentException.class,
                () -> useCase.updateRole(inputData, output -> {
                }));
    }

    @Test
    void updateRole_preservesRecordVersion() {
        var inputData = new RoleInputData(
                ROLE_ID.toString(),
                UPDATED_NAME,
                UPDATED_INVARIANT_KEY,
                UPDATED_DESCRIPTION
        );
        final AtomicReference<RoleOutputData> result = new AtomicReference<>();

        useCase.updateRole(inputData, output -> result.set(output));

        var updatedRole = roleGateway.findById(ROLE_ID);
        assertEquals(1L, updatedRole.recordVersion());
    }

    @Test
    void updateRole_updatesWhenEditedDate() {
        var inputData = new RoleInputData(
                ROLE_ID.toString(),
                UPDATED_NAME,
                UPDATED_INVARIANT_KEY,
                UPDATED_DESCRIPTION
        );
        final AtomicReference<RoleOutputData> result = new AtomicReference<>();

        useCase.updateRole(inputData, output -> result.set(output));

        var updatedRole = roleGateway.findById(ROLE_ID);
        assertNotNull(updatedRole.whenEdited());
    }

    @Test
    void updateRole_withNullDescription_updatesSuccessfully() {
        var inputData = new RoleInputData(
                ROLE_ID.toString(),
                UPDATED_NAME,
                UPDATED_INVARIANT_KEY,
                null
        );
        final AtomicReference<RoleOutputData> result = new AtomicReference<>();

        useCase.updateRole(inputData, output -> result.set(output));

        assertNotNull(result.get());
        assertNull(result.get().description());

        var updatedRole = roleGateway.findById(ROLE_ID);
        assertNull(updatedRole.description());
    }
}
