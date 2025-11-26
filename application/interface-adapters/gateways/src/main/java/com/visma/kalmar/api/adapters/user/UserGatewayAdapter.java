package com.visma.kalmar.api.adapters.user;

import com.visma.kalmar.api.entities.user.User;
import com.visma.kalmar.api.exception.ResourceNotFoundException;
import com.visma.kalmar.api.user.UserGateway;
import com.visma.feature.kalmar.api.user.UserRepository;

import java.util.UUID;

public class UserGatewayAdapter implements UserGateway {

    private final UserRepository userRepository;

    public UserGatewayAdapter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User save(User user) {
        var userEntity = new com.visma.feature.kalmar.api.user.User(
                user.idUser(),
                user.idLanguage(),
                user.email(),
                user.firstName(),
                user.lastName(),
                user.recordVersion(),
                user.whenEdited()
        );

        var savedUser = userRepository.save(userEntity);

        return new User(
                savedUser.getIdUser(),
                savedUser.getIdLanguage(),
                savedUser.getEmail(),
                savedUser.getFirstName(),
                savedUser.getLastName(),
                savedUser.getRecordVersion(),
                savedUser.getWhenEdited()

        );
    }

    @Override
    public User findById(UUID userId) {
        return userRepository.findById(userId)
                .map(this::toDomainUser)
                .orElseThrow(() -> new ResourceNotFoundException("User", "User not found with id: " + userId));
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(this::toDomainUser)
                .orElseThrow(() -> new ResourceNotFoundException("User", "User not found with email: " + email));
    }

    @Override
    public User update(User user) {
        // Verify user exists first
        userRepository.findById(user.idUser())
                .orElseThrow(() -> new ResourceNotFoundException("User", "User not found with id: " + user.idUser()));

        var userEntity = new com.visma.feature.kalmar.api.user.User(
                user.idUser(),
                user.idLanguage(),
                user.email(),
                user.firstName(),
                user.lastName(),
                user.recordVersion(),
                user.whenEdited()
        );

        var updatedUser = userRepository.save(userEntity);

        return new User(
                updatedUser.getIdUser(),
                updatedUser.getIdLanguage(),
                updatedUser.getEmail(),
                updatedUser.getFirstName(),
                updatedUser.getLastName(),
                updatedUser.getRecordVersion(),
                updatedUser.getWhenEdited()
        );
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean existsById(UUID userId) {
        return userRepository.existsById(userId);
    }

    @Override
    public void deleteById(UUID userId) {
        // Find the user to verify it exists before deleting
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User",
                        "User not found with id: " + userId));

        // Delete the user entity
        userRepository.delete(user);
    }

    private User toDomainUser(com.visma.feature.kalmar.api.user.User userEntity) {
        return new User(
                userEntity.getIdUser(),
                userEntity.getIdLanguage(),
                userEntity.getEmail(),
                userEntity.getFirstName(),
                userEntity.getLastName(),
                userEntity.getRecordVersion(),
                userEntity.getWhenEdited()
        );
    }
}
