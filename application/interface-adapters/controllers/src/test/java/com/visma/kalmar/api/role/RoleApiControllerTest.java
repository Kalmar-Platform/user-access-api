package com.visma.kalmar.api.role;

import com.visma.kalmar.api.role.dto.RoleRequest;
import com.visma.kalmar.api.role.dto.RoleResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class RoleApiControllerTest {

    private static final String ROLE_ID_STRING = "123e4567-e89b-12d3-a456-426614174000";
    private static final UUID ROLE_ID = UUID.fromString(ROLE_ID_STRING);
    private static final String NAME = "Administrator";
    private static final String INVARIANT_KEY = "ADMIN";
    private static final String DESCRIPTION = "Full system access";

    @Mock
    private CreateRoleInputPort createRoleInputPort;

    @Mock
    private GetRoleInputPort getRoleInputPort;

    @Mock
    private UpdateRoleInputPort updateRoleInputPort;

    @Mock
    private DeleteRoleInputPort deleteRoleInputPort;

    @Mock
    private RolePresenter rolePresenter;

    private RoleApiController roleApiController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        roleApiController = new RoleApiController(
                createRoleInputPort,
                getRoleInputPort,
                updateRoleInputPort,
                deleteRoleInputPort,
                rolePresenter
        );
    }

    @Test
    void getRoleById_success() {
        RoleResponse expectedResponse = createRoleResponse();
        ResponseEntity<RoleResponse> expectedEntity = ResponseEntity.ok(expectedResponse);

        when(rolePresenter.getResponse()).thenReturn(expectedEntity);

        ResponseEntity<RoleResponse> response = roleApiController.getRoleById(ROLE_ID_STRING);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());

        verify(getRoleInputPort, times(1)).getRole(eq(ROLE_ID), eq(rolePresenter));
    }

    @Test
    void createRole_success() {
        RoleRequest roleRequest = createRoleRequest();
        RoleResponse expectedResponse = createRoleResponse();
        ResponseEntity<RoleResponse> expectedEntity = ResponseEntity.status(HttpStatus.CREATED).body(expectedResponse);

        when(rolePresenter.getResponse()).thenReturn(expectedEntity);

        ResponseEntity<RoleResponse> response = roleApiController.createRole(roleRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());

        ArgumentCaptor<RoleInputData> inputDataCaptor = ArgumentCaptor.forClass(RoleInputData.class);
        verify(createRoleInputPort, times(1)).createRole(inputDataCaptor.capture(), eq(rolePresenter));

        RoleInputData capturedInputData = inputDataCaptor.getValue();
        assertEquals(NAME, capturedInputData.name());
        assertEquals(INVARIANT_KEY, capturedInputData.invariantKey());
        assertEquals(DESCRIPTION, capturedInputData.description());
    }

    @Test
    void updateRole_success() {
        RoleRequest roleRequest = createRoleRequest();
        RoleResponse expectedResponse = createRoleResponse();
        ResponseEntity<RoleResponse> expectedEntity = ResponseEntity.ok(expectedResponse);

        when(rolePresenter.getResponse()).thenReturn(expectedEntity);

        ResponseEntity<RoleResponse> response = roleApiController.updateRole(ROLE_ID_STRING, roleRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());

        ArgumentCaptor<RoleInputData> inputDataCaptor = ArgumentCaptor.forClass(RoleInputData.class);
        verify(updateRoleInputPort, times(1)).updateRole(inputDataCaptor.capture(), eq(rolePresenter));

        RoleInputData capturedInputData = inputDataCaptor.getValue();
        assertEquals(ROLE_ID_STRING, capturedInputData.roleId());
        assertEquals(NAME, capturedInputData.name());
        assertEquals(INVARIANT_KEY, capturedInputData.invariantKey());
        assertEquals(DESCRIPTION, capturedInputData.description());
    }

    @Test
    void deleteRole_success() {
        ResponseEntity<Void> response = roleApiController.deleteRole(ROLE_ID_STRING);

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        verify(deleteRoleInputPort, times(1)).deleteRole(eq(ROLE_ID));
    }

    @Test
    void createRole_callsPresenterToGetResponse() {
        RoleRequest roleRequest = createRoleRequest();
        ResponseEntity<RoleResponse> expectedEntity = ResponseEntity.status(HttpStatus.CREATED).body(new RoleResponse());

        when(rolePresenter.getResponse()).thenReturn(expectedEntity);

        roleApiController.createRole(roleRequest);

        verify(rolePresenter, times(1)).getResponse();
    }

    @Test
    void updateRole_callsPresenterToGetResponse() {
        RoleRequest roleRequest = createRoleRequest();
        ResponseEntity<RoleResponse> expectedEntity = ResponseEntity.ok(new RoleResponse());

        when(rolePresenter.getResponse()).thenReturn(expectedEntity);

        roleApiController.updateRole(ROLE_ID_STRING, roleRequest);

        verify(rolePresenter, times(1)).getResponse();
    }

    @Test
    void getRoleById_callsPresenterToGetResponse() {
        ResponseEntity<RoleResponse> expectedEntity = ResponseEntity.ok(new RoleResponse());

        when(rolePresenter.getResponse()).thenReturn(expectedEntity);

        roleApiController.getRoleById(ROLE_ID_STRING);

        verify(rolePresenter, times(1)).getResponse();
    }

    private RoleRequest createRoleRequest() {
        RoleRequest request = new RoleRequest();
        request.setName(NAME);
        request.setInvariantKey(INVARIANT_KEY);
        request.setDescription(DESCRIPTION);
        return request;
    }

    private RoleResponse createRoleResponse() {
        RoleResponse response = new RoleResponse();
        response.setRoleId(ROLE_ID_STRING);
        response.setName(NAME);
        response.setInvariantKey(INVARIANT_KEY);
        response.setDescription(DESCRIPTION);
        return response;
    }
}
