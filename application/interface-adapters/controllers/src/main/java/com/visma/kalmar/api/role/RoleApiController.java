package com.visma.kalmar.api.role;

import com.visma.kalmar.api.role.dto.RoleRequest;
import com.visma.kalmar.api.role.dto.RoleResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class RoleApiController implements RoleApi {

    private final CreateRoleInputPort createRoleInputPort;
    private final GetRoleInputPort getRoleInputPort;
    private final UpdateRoleInputPort updateRoleInputPort;
    private final DeleteRoleInputPort deleteRoleInputPort;
    private final RolePresenter rolePresenter;

    @Autowired
    public RoleApiController(
            CreateRoleInputPort createRoleInputPort,
            GetRoleInputPort getRoleInputPort,
            UpdateRoleInputPort updateRoleInputPort,
            DeleteRoleInputPort deleteRoleInputPort) {
        this(createRoleInputPort, getRoleInputPort, updateRoleInputPort, deleteRoleInputPort, new RolePresenter());
    }

    RoleApiController(
            CreateRoleInputPort createRoleInputPort,
            GetRoleInputPort getRoleInputPort,
            UpdateRoleInputPort updateRoleInputPort,
            DeleteRoleInputPort deleteRoleInputPort,
            RolePresenter rolePresenter) {
        this.createRoleInputPort = createRoleInputPort;
        this.getRoleInputPort = getRoleInputPort;
        this.updateRoleInputPort = updateRoleInputPort;
        this.deleteRoleInputPort = deleteRoleInputPort;
        this.rolePresenter = rolePresenter;
    }

    @Override
    public ResponseEntity<RoleResponse> getRoleById(String roleId) {
        UUID roleUuid = UUID.fromString(roleId);

        getRoleInputPort.getRole(roleUuid, rolePresenter);

        return rolePresenter.getResponse();
    }

    @Override
    public ResponseEntity<RoleResponse> createRole(RoleRequest roleRequest) {
        var inputData = toRoleInputData(null, roleRequest);

        createRoleInputPort.createRole(inputData, rolePresenter);

        return rolePresenter.getResponse();
    }

    @Override
    public ResponseEntity<RoleResponse> updateRole(String roleId, RoleRequest roleRequest) {
        var inputData = toRoleInputData(roleId, roleRequest);

        updateRoleInputPort.updateRole(inputData, rolePresenter);

        return rolePresenter.getResponse();
    }

    @Override
    public ResponseEntity<Void> deleteRole(String roleId) {
        UUID roleUuid = UUID.fromString(roleId);

        deleteRoleInputPort.deleteRole(roleUuid);

        return ResponseEntity.noContent().build();
    }

    private RoleInputData toRoleInputData(String roleId, RoleRequest roleRequest) {
        return new RoleInputData(
                roleId,
                roleRequest.getName(),
                roleRequest.getInvariantKey(),
                roleRequest.getDescription()
        );
    }
}
