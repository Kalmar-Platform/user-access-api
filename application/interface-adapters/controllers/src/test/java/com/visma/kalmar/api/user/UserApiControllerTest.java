package com.visma.kalmar.api.user;

import com.visma.kalmar.api.user.dto.UserResponse;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserApiControllerTest {

    private static final String USER_ID_STRING = "123e4567-e89b-12d3-a456-426614174000";
    private static final UUID USER_ID = UUID.fromString(USER_ID_STRING);
    private static final String EMAIL = "test@example.com";
    private static final String FIRST_NAME = "John";
    private static final String LAST_NAME = "Doe";
    private static final String LANGUAGE_CODE = "en";

    @Mock
    private CreateUserInputPort createUserInputPort;

    @Mock
    private UpdateUserInputPort updateUserInputPort;

    @Mock
    private DeleteUserInputPort deleteUserInputPort;

    @Mock
    private GetUserInputPort getUserInputPort;

    @Mock
    private UserPresenter userPresenter;

    private UserApiController userApiController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        userApiController = new UserApiController(
                createUserInputPort,
                updateUserInputPort,
                deleteUserInputPort,
                getUserInputPort,
                userPresenter
        );
    }

    @Test
    void getUserById_success() {
        UserResponse expectedResponse = createUserResponse();
        ResponseEntity<UserResponse> expectedEntity = ResponseEntity.ok(expectedResponse);

        when(userPresenter.getResponse()).thenReturn(expectedEntity);

        ResponseEntity<UserResponse> response = userApiController.getUserById(USER_ID_STRING);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());

        ArgumentCaptor<UUID> userIdCaptor = ArgumentCaptor.forClass(UUID.class);
        verify(getUserInputPort).getUserById(userIdCaptor.capture(), eq(userPresenter));
        assertEquals(USER_ID, userIdCaptor.getValue());
    }

    @Test
    void getUserByEmail_success() {
        UserResponse expectedResponse = createUserResponse();
        ResponseEntity<UserResponse> expectedEntity = ResponseEntity.ok(expectedResponse);

        when(userPresenter.getResponse()).thenReturn(expectedEntity);

        ResponseEntity<UserResponse> response = userApiController.getUserByEmail(EMAIL);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());

        verify(getUserInputPort).getUserByEmail(eq(EMAIL), eq(userPresenter));
    }

    private UserResponse createUserResponse() {
        UserResponse response = new UserResponse();
        response.setUserId(USER_ID_STRING);
        response.setEmail(EMAIL);
        response.setFirstName(FIRST_NAME);
        response.setLastName(LAST_NAME);
        response.setLanguageCode(LANGUAGE_CODE);
        return response;
    }
}
