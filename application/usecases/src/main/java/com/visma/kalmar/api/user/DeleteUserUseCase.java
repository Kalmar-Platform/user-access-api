package com.visma.kalmar.api.user;

import com.visma.kalmar.api.exception.ConnectUserException;
import com.visma.kalmar.api.vismaconnect.VismaConnectUserGateway;

import java.util.UUID;

/**
 * Use case for deleting an existing user.
 * Handles user deletion from both local database and Visma Connect.
 * The user will be unlinked from Visma Connect and deleted from the Feature database.
 */
public class DeleteUserUseCase implements DeleteUserInputPort {

    private final UserGateway userGateway;
    private final VismaConnectUserGateway vismaConnectUserGateway;

    public DeleteUserUseCase(UserGateway userGateway,
                             VismaConnectUserGateway vismaConnectUserGateway) {
        this.userGateway = userGateway;
        this.vismaConnectUserGateway = vismaConnectUserGateway;
    }

    @Override
    public void deleteUser(UUID userId) {
        // Check if user exists (this will throw ResourceNotFoundException if not found)
        userGateway.findById(userId);

        // Delete user from database (we know Connect unlink succeeded or was already unlinked)
        userGateway.deleteById(userId);

        // No need to present result for delete operations that return 204 No Content
    }
}
