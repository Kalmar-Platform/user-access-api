package com.visma.kalmar.api.adapters.user;

import com.visma.kalmar.api.entities.user.User;
import com.visma.kalmar.api.exception.ResourceNotFoundException;
import com.visma.useraccess.kalmar.api.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserGatewayAdapterTest {

    private static final UUID USER_ID = UUID.randomUUID();
    private static final UUID LANGUAGE_ID = UUID.randomUUID();
    private static final String EMAIL = "test@example.com";
    private static final String FIRST_NAME = "John";
    private static final String LAST_NAME = "Doe";
    private static final Long RECORD_VERSION = 1L;

    @Mock
    private UserRepository userRepository;

    private UserGatewayAdapter userGatewayAdapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        userGatewayAdapter = new UserGatewayAdapter(userRepository);
    }

    @Test
    void findById_success() {
        com.visma.useraccess.kalmar.api.user.User repositoryUser = createRepositoryUser();
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(repositoryUser));

        User result = userGatewayAdapter.findById(USER_ID);

        assertNotNull(result);
        assertEquals(USER_ID, result.idUser());
        assertEquals(LANGUAGE_ID, result.idLanguage());
        assertEquals(EMAIL, result.email());
        assertEquals(FIRST_NAME, result.firstName());
        assertEquals(LAST_NAME, result.lastName());
        verify(userRepository).findById(USER_ID);
    }

    @Test
    void findById_notFound() {
        when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> userGatewayAdapter.findById(USER_ID));
        verify(userRepository).findById(USER_ID);
    }

    @Test
    void findByEmail_success() {
        com.visma.useraccess.kalmar.api.user.User repositoryUser = createRepositoryUser();
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(repositoryUser));

        User result = userGatewayAdapter.findByEmail(EMAIL);

        assertNotNull(result);
        assertEquals(USER_ID, result.idUser());
        assertEquals(LANGUAGE_ID, result.idLanguage());
        assertEquals(EMAIL, result.email());
        assertEquals(FIRST_NAME, result.firstName());
        assertEquals(LAST_NAME, result.lastName());
        verify(userRepository).findByEmail(EMAIL);
    }

    @Test
    void findByEmail_notFound() {
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> userGatewayAdapter.findByEmail(EMAIL));
        verify(userRepository).findByEmail(EMAIL);
    }

    private com.visma.useraccess.kalmar.api.user.User createRepositoryUser() {
        return new com.visma.useraccess.kalmar.api.user.User(
                USER_ID,
                LANGUAGE_ID,
                EMAIL,
                FIRST_NAME,
                LAST_NAME,
                RECORD_VERSION,
                new Date()
        );
    }
}
