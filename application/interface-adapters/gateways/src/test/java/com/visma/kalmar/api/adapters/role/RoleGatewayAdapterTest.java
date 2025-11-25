package com.visma.kalmar.api.adapters.role;

import com.visma.kalmar.api.entities.role.Role;
import com.visma.kalmar.api.exception.ResourceNotFoundException;
import com.visma.feature.kalmar.api.role.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RoleGatewayAdapterTest {

    private static final UUID ROLE_ID = UUID.randomUUID();
    private static final String NAME = "Administrator";
    private static final String INVARIANT_KEY = "ADMIN";
    private static final String DESCRIPTION = "Full system access";
    private static final Long RECORD_VERSION = 1L;

    @Mock
    private RoleRepository roleRepository;

    private RoleGatewayAdapter roleGatewayAdapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        roleGatewayAdapter = new RoleGatewayAdapter(roleRepository);
    }

    @Test
    void save_success() {
        Role domainRole = createDomainRole();
        com.visma.feature.kalmar.api.role.Role repositoryRole = createRepositoryRole();

        when(roleRepository.save(any(com.visma.feature.kalmar.api.role.Role.class)))
                .thenReturn(repositoryRole);

        Role result = roleGatewayAdapter.save(domainRole);

        assertNotNull(result);
        assertEquals(ROLE_ID, result.idRole());
        assertEquals(NAME, result.name());
        assertEquals(INVARIANT_KEY, result.invariantKey());
        assertEquals(DESCRIPTION, result.description());
        assertEquals(RECORD_VERSION, result.recordVersion());
        verify(roleRepository, times(1)).save(any(com.visma.feature.kalmar.api.role.Role.class));
    }

    @Test
    void findById_success() {
        com.visma.feature.kalmar.api.role.Role repositoryRole = createRepositoryRole();
        when(roleRepository.findById(ROLE_ID)).thenReturn(Optional.of(repositoryRole));

        Role result = roleGatewayAdapter.findById(ROLE_ID);

        assertNotNull(result);
        assertEquals(ROLE_ID, result.idRole());
        assertEquals(NAME, result.name());
        assertEquals(INVARIANT_KEY, result.invariantKey());
        assertEquals(DESCRIPTION, result.description());
        verify(roleRepository, times(1)).findById(ROLE_ID);
    }

    @Test
    void findById_notFound() {
        when(roleRepository.findById(ROLE_ID)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> roleGatewayAdapter.findById(ROLE_ID));
        verify(roleRepository, times(1)).findById(ROLE_ID);
    }

    @Test
    void findByInvariantKey_success() {
        com.visma.feature.kalmar.api.role.Role repositoryRole = createRepositoryRole();
        when(roleRepository.findByInvariantKey(INVARIANT_KEY)).thenReturn(Optional.of(repositoryRole));

        Role result = roleGatewayAdapter.findByInvariantKey(INVARIANT_KEY);

        assertNotNull(result);
        assertEquals(ROLE_ID, result.idRole());
        assertEquals(NAME, result.name());
        assertEquals(INVARIANT_KEY, result.invariantKey());
        assertEquals(DESCRIPTION, result.description());
        verify(roleRepository, times(1)).findByInvariantKey(INVARIANT_KEY);
    }

    @Test
    void findByInvariantKey_notFound() {
        when(roleRepository.findByInvariantKey(INVARIANT_KEY)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> roleGatewayAdapter.findByInvariantKey(INVARIANT_KEY));
        verify(roleRepository, times(1)).findByInvariantKey(INVARIANT_KEY);
    }

    @Test
    void update_success() {
        Role domainRole = createDomainRole();
        com.visma.feature.kalmar.api.role.Role repositoryRole = createRepositoryRole();

        when(roleRepository.findById(ROLE_ID)).thenReturn(Optional.of(repositoryRole));
        when(roleRepository.save(any(com.visma.feature.kalmar.api.role.Role.class)))
                .thenReturn(repositoryRole);

        Role result = roleGatewayAdapter.update(domainRole);

        assertNotNull(result);
        assertEquals(ROLE_ID, result.idRole());
        assertEquals(NAME, result.name());
        assertEquals(INVARIANT_KEY, result.invariantKey());
        assertEquals(DESCRIPTION, result.description());
        verify(roleRepository, times(1)).findById(ROLE_ID);
        verify(roleRepository, times(1)).save(any(com.visma.feature.kalmar.api.role.Role.class));
    }

    @Test
    void update_roleNotFound() {
        Role domainRole = createDomainRole();

        when(roleRepository.findById(ROLE_ID)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> roleGatewayAdapter.update(domainRole));
        verify(roleRepository, times(1)).findById(ROLE_ID);
        verify(roleRepository, never()).save(any(com.visma.feature.kalmar.api.role.Role.class));
    }

    @Test
    void existsByInvariantKey_returnsTrue() {
        when(roleRepository.existsByInvariantKey(INVARIANT_KEY)).thenReturn(true);

        boolean result = roleGatewayAdapter.existsByInvariantKey(INVARIANT_KEY);

        assertTrue(result);
        verify(roleRepository, times(1)).existsByInvariantKey(INVARIANT_KEY);
    }

    @Test
    void existsByInvariantKey_returnsFalse() {
        when(roleRepository.existsByInvariantKey(INVARIANT_KEY)).thenReturn(false);

        boolean result = roleGatewayAdapter.existsByInvariantKey(INVARIANT_KEY);

        assertFalse(result);
        verify(roleRepository, times(1)).existsByInvariantKey(INVARIANT_KEY);
    }

    @Test
    void existsByName_returnsTrue() {
        when(roleRepository.existsByName(NAME)).thenReturn(true);

        boolean result = roleGatewayAdapter.existsByName(NAME);

        assertTrue(result);
        verify(roleRepository, times(1)).existsByName(NAME);
    }

    @Test
    void existsByName_returnsFalse() {
        when(roleRepository.existsByName(NAME)).thenReturn(false);

        boolean result = roleGatewayAdapter.existsByName(NAME);

        assertFalse(result);
        verify(roleRepository, times(1)).existsByName(NAME);
    }

    @Test
    void existsById_returnsTrue() {
        when(roleRepository.existsById(ROLE_ID)).thenReturn(true);

        boolean result = roleGatewayAdapter.existsById(ROLE_ID);

        assertTrue(result);
        verify(roleRepository, times(1)).existsById(ROLE_ID);
    }

    @Test
    void existsById_returnsFalse() {
        when(roleRepository.existsById(ROLE_ID)).thenReturn(false);

        boolean result = roleGatewayAdapter.existsById(ROLE_ID);

        assertFalse(result);
        verify(roleRepository, times(1)).existsById(ROLE_ID);
    }

    @Test
    void deleteById_success() {
        com.visma.feature.kalmar.api.role.Role repositoryRole = createRepositoryRole();

        when(roleRepository.findById(ROLE_ID)).thenReturn(Optional.of(repositoryRole));

        roleGatewayAdapter.deleteById(ROLE_ID);

        verify(roleRepository, times(1)).findById(ROLE_ID);
        verify(roleRepository, times(1)).delete(repositoryRole);
    }

    @Test
    void deleteById_roleNotFound() {
        when(roleRepository.findById(ROLE_ID)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> roleGatewayAdapter.deleteById(ROLE_ID));
        verify(roleRepository, times(1)).findById(ROLE_ID);
        verify(roleRepository, never()).delete(any(com.visma.feature.kalmar.api.role.Role.class));
    }

    private Role createDomainRole() {
        return new Role(
                ROLE_ID,
                NAME,
                INVARIANT_KEY,
                DESCRIPTION,
                RECORD_VERSION,
                new Date()
        );
    }

    private com.visma.feature.kalmar.api.role.Role createRepositoryRole() {
        return new com.visma.feature.kalmar.api.role.Role(
                ROLE_ID,
                NAME,
                INVARIANT_KEY,
                DESCRIPTION,
                RECORD_VERSION,
                new Date()
        );
    }
}
