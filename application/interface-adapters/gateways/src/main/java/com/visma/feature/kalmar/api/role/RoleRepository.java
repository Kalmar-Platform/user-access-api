package com.visma.feature.kalmar.api.role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {

    Optional<Role> findByInvariantKey(String invariantKey);

    boolean existsByInvariantKey(String invariantKey);

    boolean existsByName(String name);
}
