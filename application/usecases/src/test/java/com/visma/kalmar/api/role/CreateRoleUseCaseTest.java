package com.visma.kalmar.api.role;

import com.visma.kalmar.api.exception.ResourceAlreadyExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

class CreateRoleUseCaseTest {

    private static final String NAME = "Administrator";
    private static final String INVARIANT_KEY = "ADMIN";
    private static final String DESCRIPTION = "Full system access";

    private InMemoryRoleGatewayAdapter roleGateway;
    private CreateRoleUseCase useCase;

    @BeforeEach
    void setUp() {
        roleGateway = new InMemoryRoleGatewayAdapter();
        useCase = new CreateRoleUseCase(roleGateway);
    }

    @Test
    void createRole_withValidData_createsRoleSuccessfully() {
        var inputData = new RoleInputData(null, NAME, INVARIANT_KEY, DESCRIPTION);
        final AtomicReference<RoleOutputData> result = new AtomicReference<>();

        useCase.createRole(inputData, output -> result.set(output));

        assertNotNull(result.get());
        assertNotNull(result.get().roleId());
        assertEquals(NAME, result.get().name());
        assertEquals(INVARIANT_KEY, result.get().invariantKey());
        assertEquals(DESCRIPTION, result.get().description());
        assertTrue(result.get().created());

        var savedRole = roleGateway.findByInvariantKey(INVARIANT_KEY);
        assertEquals(NAME, savedRole.name());
        assertEquals(INVARIANT_KEY, savedRole.invariantKey());
        assertEquals(DESCRIPTION, savedRole.description());
    }

    @Test
    void createRole_withExistingInvariantKey_throwsResourceAlreadyExistsException() {
        var inputData = new RoleInputData(null, NAME, INVARIANT_KEY, DESCRIPTION);

        useCase.createRole(inputData, output -> {
        });

        var duplicateInputData = new RoleInputData(null, "Another Name", INVARIANT_KEY, "Different description");

        var exception = assertThrows(ResourceAlreadyExistsException.class,
                () -> useCase.createRole(duplicateInputData, output -> {
                }));

        assertTrue(exception.getMessage().contains("Role with invariantKey: " + INVARIANT_KEY + " already exists"));
    }

    @Test
    void createRole_withExistingName_throwsResourceAlreadyExistsException() {
        var inputData = new RoleInputData(null, NAME, INVARIANT_KEY, DESCRIPTION);

        useCase.createRole(inputData, output -> {
        });

        var duplicateInputData = new RoleInputData(null, NAME, "DIFFERENT_KEY", "Different description");

        var exception = assertThrows(ResourceAlreadyExistsException.class,
                () -> useCase.createRole(duplicateInputData, output -> {
                }));

        assertTrue(exception.getMessage().contains("Role with name: " + NAME + " already exists"));
    }

    @Test
    void createRole_setsRecordVersionToOne() {
        var inputData = new RoleInputData(null, NAME, INVARIANT_KEY, DESCRIPTION);
        final AtomicReference<RoleOutputData> result = new AtomicReference<>();

        useCase.createRole(inputData, output -> result.set(output));

        var savedRole = roleGateway.findByInvariantKey(INVARIANT_KEY);
        assertEquals(1L, savedRole.recordVersion());
    }

    @Test
    void createRole_setsWhenEditedDate() {
        var inputData = new RoleInputData(null, NAME, INVARIANT_KEY, DESCRIPTION);
        final AtomicReference<RoleOutputData> result = new AtomicReference<>();

        useCase.createRole(inputData, output -> result.set(output));

        var savedRole = roleGateway.findByInvariantKey(INVARIANT_KEY);
        assertNotNull(savedRole.whenEdited());
    }

    @Test
    void createRole_withNullDescription_createsRoleSuccessfully() {
        var inputData = new RoleInputData(null, NAME, INVARIANT_KEY, null);
        final AtomicReference<RoleOutputData> result = new AtomicReference<>();

        useCase.createRole(inputData, output -> result.set(output));

        assertNotNull(result.get());
        assertEquals(NAME, result.get().name());
        assertEquals(INVARIANT_KEY, result.get().invariantKey());
        assertNull(result.get().description());

        var savedRole = roleGateway.findByInvariantKey(INVARIANT_KEY);
        assertNull(savedRole.description());
    }
}
